package org.usfirst.frc862.vision;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import org.usfirst.frc862.util.CrashTrackingRunnable;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.vision.messages.HeartbeatMessage;
import org.usfirst.frc862.vision.messages.OffWireMessage;
import org.usfirst.frc862.vision.messages.VisionMessage;

import edu.wpi.first.wpilibj.Timer;

/**
 * This controls all vision actions, including vision updates, capture, and
 * interfacing with the Android phone with Android Debug Bridge. It also stores
 * all VisionUpdates (from the Android phone) and contains methods to add
 * to/prune the VisionUpdate list. Much like the subsystems, outside methods get
 * the VisionServer instance (there is only one VisionServer) instead of
 * creating new VisionServer instances.
 * 
 * @see VisionUpdate.java
 */

public class VisionServer extends CrashTrackingRunnable {

    private static VisionServer s_instance = null;
    private ServerSocket m_server_socket;
    private boolean m_running = true;
    private int m_port;
    private ArrayList<VisionUpdateReceiver> receivers = new ArrayList<>();
    AdbBridge adb = new AdbBridge();
    double lastMessageReceivedTime = 0;
    private boolean m_use_java_time = false;

    private ArrayList<ServerThread> serverThreads = new ArrayList<>();
    private volatile boolean mWantsAppRestart = false;

    public static VisionServer getInstance() {
        if (s_instance == null) {
            s_instance = new VisionServer(Constants.kAndroidAppTcpPort);
        }
        return s_instance;
    }

    private boolean mIsConnect = false;

    public boolean isConnected() {
        return mIsConnect;
    }

    public void requestAppRestart() {
        mWantsAppRestart = true;
    }

    protected class ServerThread extends CrashTrackingRunnable {
        private Socket m_socket;

        public ServerThread(Socket socket) {
            m_socket = socket;
        }

        public void send(VisionMessage message) {
            String toSend = message.toJson() + "\n";
            if (m_socket != null && m_socket.isConnected()) {
                try {
                    OutputStream os = m_socket.getOutputStream();
                    os.write(toSend.getBytes());
                } catch (IOException e) {
                    System.err.println("VisionServer: Could not send data to socket");
                }
            }
        }

        public void handleMessage(VisionMessage message, double timestamp) {
            if ("targets".equals(message.getType())) {
                VisionUpdate update = VisionUpdate.generateFromJsonString(timestamp, message.getMessage());
                receivers.removeAll(Collections.singleton(null));
                if (update.isValid()) {
                    for (VisionUpdateReceiver receiver : receivers) {
                        receiver.gotUpdate(update);
                    }
                }
            }
            if ("heartbeat".equals(message.getType())) {
                send(HeartbeatMessage.getInstance());
            }
        }

        public boolean isAlive() {
            return m_socket != null && m_socket.isConnected() && !m_socket.isClosed();
        }

        @Override
        public void runCrashTracked() {
            if (m_socket == null) {
                Logger.debug("Socket is null");
                return;
            }
            try {
                InputStream is = m_socket.getInputStream();
                byte[] buffer = new byte[2048];
                int read;
                while (m_socket.isConnected() && (read = is.read(buffer)) != -1) {
                    double timestamp = getTimestamp();
                    lastMessageReceivedTime = timestamp;
                    Logger.debug("We have a message");
                    String messageRaw = new String(buffer, 0, read);
                    String[] messages = messageRaw.split("\n");
                    for (String message : messages) {
                        OffWireMessage parsedMessage = new OffWireMessage(message);
                        if (parsedMessage.isValid()) {
                            handleMessage(parsedMessage, timestamp);
                        }
                    }
                }
                Logger.debug("Socket is disconnected");
            } catch (IOException e) {
                Logger.debug("Could not talk to socket");
            }
            if (m_socket != null) {
                try {
                    Logger.info("closing socket");
                    m_socket.close();
                } catch (IOException e) {
                    Logger.error("Socket Error: " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Instantializes the VisionServer and connects to ADB via the specified
     * port.
     * 
     * @param Port
     */
    private VisionServer(int port) {
        try {
            adb = new AdbBridge();
            m_port = port;
            System.out.println("Try port " + port);
            Logger.debug("Try port " + port);
            m_server_socket = new ServerSocket(port);
            adb.start();
            adb.reversePortForward(port, port);
            Logger.debug("Try and start app");
            adb.restartApp();
            try {
                String useJavaTime = System.getenv("USE_JAVA_TIME");
                m_use_java_time = "true".equals(useJavaTime);
            } catch (NullPointerException e) {
                m_use_java_time = false;
            }
        } catch (IOException e) {
            Logger.error("Vision error: " + e);
            e.printStackTrace();
        }
        Logger.debug("Starting thread");
        new Thread(this).start();
        Logger.debug("Starting app thread");
        new Thread(new AppMaintainanceThread()).start();
    }

    public void restartAdb() {
        Logger.debug("restartAdb");
        adb.restartAdb();
        adb.reversePortForward(m_port, m_port);
    }

    public void restartApp() {
        adb.restartApp();
    }
    
    /**
     * If a VisionUpdate object (i.e. a target) is not in the list, add it.
     * 
     * @see VisionUpdate
     */
    public void addVisionUpdateReceiver(VisionUpdateReceiver receiver) {
        if (!receivers.contains(receiver)) {
            receivers.add(receiver);
        }
    }

    public void removeVisionUpdateReceiver(VisionUpdateReceiver receiver) {
        if (receivers.contains(receiver)) {
            receivers.remove(receiver);
        }
    }

    @Override
    public void runCrashTracked() {
        while (m_running) {
            try {
                Socket p = m_server_socket.accept();
                ServerThread s = new ServerThread(p);
                new Thread(s).start();
                serverThreads.add(s);
            } catch (IOException e) {
                System.err.println("Issue accepting socket connection!");
            } finally {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class AppMaintainanceThread extends CrashTrackingRunnable {
        @Override
        public void runCrashTracked() {
            while (true) {
                if (getTimestamp() - lastMessageReceivedTime > 0.1) {
                    // camera disconnected
                    Logger.debug("reverse port from app thread " + Timer.getFPGATimestamp());
//                    adb.reversePortForward(m_port, m_port);
                    restartAdb();
                    mIsConnect = false;
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    mIsConnect = true;
                }
                if (mWantsAppRestart) {
                    adb.restartApp();
                    mWantsAppRestart = false;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private double getTimestamp() {
        if (m_use_java_time) {
            return System.currentTimeMillis();
        } else {
            return Timer.getFPGATimestamp();
        }
    }

    public void restartEverything() {
    }
}
