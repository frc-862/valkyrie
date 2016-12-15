package org.usfirst.frc862.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleSupplier;

import org.usfirst.frc862.valkyrie.Constants;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

public class DataLogger {
    private static DataLogger logger;
    private BufferedWriter writer;
    private ArrayList<String> fieldNames = new ArrayList<String>();
    private ArrayList<DoubleSupplier> fieldValues = new ArrayList<DoubleSupplier>();
    private Notifier worker;
    private boolean first_time = true;
    
    private static DataLogger getLogger() {
        if (logger == null) {
            logger = new DataLogger();
        }
        return logger;
    }

    public static void addDataElement(String name, DoubleSupplier val) {
        DataLogger me = DataLogger.getLogger();
        me.fieldNames.add(name);
        me.fieldValues.add(val);
    }
    
    public void startWorker() {
        if (first_time) {
            writeHeader();
            first_time = false;
        }
        worker.startPeriodic(Constants.dataLoggerPeriod);
    }
    
    public void stopWorker() {
        worker.stop();
    }
    
    public static void start() {
        DataLogger.getLogger().startWorker();
    }
    
    public static void stop() {
        DataLogger.getLogger().stopWorker();
    }
    
    private void writeHeader() {
        try {
            writer.write("timestamp");
            for (String fld : fieldNames) {
                writer.write(",");
                
                // TODO do we need to catch exceptions here
                // to ensure broken sensors/values don't
                // goof up the rest of the log?
                writer.write(fld);
            }
            writer.newLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void writeValues() {
        try {
            writer.write(Double.toString(Timer.getFPGATimestamp()));
            for (DoubleSupplier fld : fieldValues) {
                writer.write(",");
                writer.write(Double.toString(fld.getAsDouble()));
            }
            writer.newLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private DataLogger() {
        try {
            File file = logFileName();
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

            worker = new Notifier(() -> {
                writeValues();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File logFileName() {
        File base = null;

        // find the mount point
        char mount = 'u';
        while (base == null && mount <= 'z') {
            File f = new File("/" + mount);
            if (f.isDirectory()) {
                base = f;
            }
            ++mount;
        }

        if (base == null) {
            base = new File("/home/lvuser");
        }

        base = new File(base, "log");
        base.mkdirs();

        int counter = 0;
        File result = new File(base, String.format("data-%05d.log", counter));
        while (result.exists()) {
            result = new File(base, String.format("data-%05d.log", ++counter));
        }

        return result;
    }

    public static void flush() {
        try {
            getLogger().writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("Error flushing logger stream");
            e.printStackTrace();
        }
    }
}
