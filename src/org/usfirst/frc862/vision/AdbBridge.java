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
            Logger.debug("Cmd: " + cmd);
            
            p.waitFor();
            Logger.debug("Return: " + p.exitValue());
            
            byte[] b = new byte[2048];
            p.getInputStream().read(b);
            Logger.debug("Output " + new String(b));
            
//            byte[] c = new byte[2048];
//            p.getErrorStream().read(b);
//            Logger.debug("Error " + new String(c));
            
            Logger.flush();            
        } catch (IOException e) {
            Logger.error("AdbBridge: Could not run command " + cmd);
            Logger.error(e.toString());
            return false;
        } catch (InterruptedException e) {
            Logger.error("AdbBridge: Could not run command " + cmd);
            Logger.error(e.toString());
            return false;
        }
        return true;
    }

    public void start() {
        Logger.debug("Starting adb");
//        runCommand("start-server");
        runCommand("usb");
        runCommand("devices");
    }

    public void stop() {
        Logger.debug("Stopping adb");
        runCommand("kill-server");
    }

    public void restartAdb() {
        Logger.debug("Restarting adb");
        stop();
        start();
    }

    public void portForward(int local_port, int remote_port) {
        runCommand("forward tcp:" + local_port + " tcp:" + remote_port);
    }

    public void reversePortForward(int remote_port, int local_port) {
        runCommand("reverse tcp:" + remote_port + " tcp:" + local_port);
    }

    public void restartApp() {
        Logger.debug("Restarting app");
        runCommand("shell am force-stop com.team254.cheezdroid");
        runCommand("am start com.team254.cheezdroid/com.team254.cheezdroid.VisionTrackerActivity");
    }
}
