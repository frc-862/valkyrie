package org.usfirst.frc862.vision;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.usfirst.frc862.util.Logger;

/**
 * AdbBridge interfaces to an Android Debug Bridge (adb) binary, which is needed
 * to communicate to Android devices over USB.
 *
 * adb binary provided by https://github.com/Spectrum3847/RIOdroid
 */
public class AdbBridge {
    Path bin_location_;
    public final static Path DEFAULT_LOCATION = Paths.get("/usr/bin/adb");

    public AdbBridge() {
        Path adb_location;
        String env_val = System.getenv("FRC_ADB_LOCATION");
        if (env_val == null || "".equals(env_val)) {
            adb_location = DEFAULT_LOCATION;
        } else {
            adb_location = Paths.get(env_val);
        }
        bin_location_ = adb_location;
    }

    public AdbBridge(Path location) {
        bin_location_ = location;
    }

    private boolean runCommand(String args) {
        Runtime r = Runtime.getRuntime();
        String cmd = bin_location_.toString() + " " + args;

        try {
            Process p = r.exec(cmd);
            Logger.debug("CMD: " + cmd);
            
            p.waitFor();
            Logger.debug("Return: " + p.exitValue());

            byte[] b = new byte[2048];
            p.getInputStream().read(b);
            Logger.debug("Output " + new String(b));
            
            Logger.flush();
        } catch (IOException e) {
            System.err.println("AdbBridge: Could not run command " + cmd);
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            System.err.println("AdbBridge: Could not run command " + cmd);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void start() {
        Logger.debug("Start ADB");
        runCommand("start-server");
    }

    public void stop() {
        Logger.debug("Stop ADB");
        runCommand("kill-server");
    }

    public void restartAdb() {
        stop();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        start();
    }

    public void portForward(int local_port, int remote_port) {
        runCommand("forward tcp:" + local_port + " tcp:" + remote_port);
    }

    public void reversePortForward(int remote_port, int local_port) {
        runCommand("reverse tcp:" + remote_port + " tcp:" + local_port);
    }

    public void startApp() {
        Logger.debug("Starting android app");
        runCommand("shell am start com.team254.cheezdroid/com.team254.cheezdroid.VisionTrackerActivity");        
    }
    
    public void stopApp() {
        Logger.debug("Stopping android app");
        runCommand("shell am force-stop com.team254.cheezdroid");
    }
    
    public void restartApp() {
        stopApp();
        startApp();
    }
}
