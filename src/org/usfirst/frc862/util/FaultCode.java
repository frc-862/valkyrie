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

public class FaultCode {
    public enum Codes {
        LEFT_ENCODER_NOT_FOUND, RIGHT_ENCODER_NOT_FOUND, LOW_MAIN_VOLTAGE
    }

    static HashSet<Codes> faults = new HashSet<>();
    static boolean first_time = true;

    public static Path getFaultPath() {
        return Paths.get("/home/lvuser/faults.log");
    }

    public static void write(Codes code) {
        try {
            if (first_time) {
                Files.write(getFaultPath(), ("######### RESTART #########\n").getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                first_time = false;
            }
            
            if (!faults.contains(code)) {
                faults.add(code);
                Files.write(Paths.get("/home/lvuser/faults.log"),
                        ("FAULT Detected: " + code.toString() + "\n").getBytes(), StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                Logger.error("FAULT: " + code);
                // TODO Write fault to the dashboard
            }
        } catch (IOException e) {
            Logger.error("Unable to write fault code " + code);
            e.printStackTrace();
        }
    }
}
