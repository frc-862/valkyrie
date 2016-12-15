package org.usfirst.frc862.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FaultCode {
    public enum Codes {
        LEFT_ENCODER_NOT_FOUND, RIGHT_ENCODER_NOT_FOUND,
        LOW_MAIN_VOLTAGE, SLOW_LOOPER
    }

    private static HashSet<Codes> faults = new HashSet<>();
    private static boolean first_time = true;

    private static Path getFaultPath() {
        return Paths.get("/home/lvuser/faults.log");
    }

    public static void write(Codes code) {
        write(code, "");
    }
    
    public static void write(Codes code, String msg) {
        try {
            if (first_time) {
                for (Codes c : Codes.values()) {
                    SmartDashboard.putBoolean("FAULT_" + c.toString(), true);
                }
                Files.write(getFaultPath(), ("######### RESTART #########\n").getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                first_time = false;
            }
            
            if (!faults.contains(code)) {
                faults.add(code);
                Files.write(Paths.get("/home/lvuser/faults.log"),
                        ("FAULT Detected: " + code.toString() + " " + msg + "\n").getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                Logger.error("FAULT: " + code + " " + msg);
                SmartDashboard.putBoolean("FAULT_" + code.toString(), false);
            }
        } catch (IOException e) {
            Logger.error("Unable to write fault code " + code);
            e.printStackTrace();
        }
    }
}
