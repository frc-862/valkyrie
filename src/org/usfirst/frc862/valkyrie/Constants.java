package org.usfirst.frc862.valkyrie;

import org.usfirst.frc862.util.ConstantsBase;
import org.usfirst.frc862.util.InterpolatedMap;

@SuppressWarnings("WeakerAccess")
public class Constants extends ConstantsBase {
    public static final double CrashDuration = 0.1;
    public static final double LostBattleTriggerTime = 0.75;
    public static final double CompressorPauseTime = 1;
    // Volts per second
    public static double driveRampRate = 300;
    public static int encoderTicksPerRev = 360;
    public static double minimumShiftDelay = 0.75;
    public static InterpolatedMap lookupTable = new InterpolatedMap(new Double[]{1.0,2.1,3.2,4.3});
    
    public static double brakeP = 0.5;
    public static double brakeI = 0;
    public static double brakeD = 0;
    public static double brakeF = 0;
    public static int brakeRampRate = 0;
    
    // Note this is final, it will not be
    // in the config file, and if you put
    // it there, the value will be ignored
    // as the value here in the code is 
    // immutable
    public static int brakeSlot = 1;
    public static double CoastVelocity = 400;
    public static double kDriveBaseLockKp = 0.5;
    public static double kDriveBaseLockKi = 0;
    public static double kDriveBaseLockKd = 0;
    public static double kDriveBaseLockKf = 0;
    public static int kDriveBaseLockIZone = 0;
    public static double kDriveBaseLockRampRate = 0;
    public static int kDriveBaseLockAllowableError = 10;
    public static double brakeIZone;
    public static double driveTrainLoopRate = 0.05;
    public static double maxRampRate = 0.75;
    public static double shiftDelay = 0.25;
    public static double dataLoggerPeriod = 0.05;  // 20 times a second by default 
    public static int logDepth = 1000;
    public static double backgroundLoopRate = 0.5;
    public static double deadband = 0.1;
    // public static double maxVelocity = (4 * 12.0) / (6 * 3.1415) * 60; // in rpm w/6" wheel
    public static double maxVelocity = 500;
    public static double winchRampTime = 3;
    public static double LowVoltage = 10;
    public static double HCTriggerTime = 0.25;
    public static double HighCurrentThreshold = 200;
    public static double CrashDeacceleration = -200;
    public static double CoastTriggerTime = 0.01;
    public static double UpshiftVelocity = 300;
    public static double VelocityUpshiftTime = 0.25;
    public static double MinRequestedPowerForUpshift = 0.75;
    public static double LVTriggerTime = 0.5;
    public static double MaxCoastPower = 0.35;
    public static double MinUpshiftVelocity = 300;
    public static double straightCommandDelta = 0.19;
    public static double rotateEpsilon = 2.5;
    public static double rotatePGain = maxVelocity / 180.0 * 1.5;
    public static double straightenPGain = 1 / 90.0;

    public String getFileName() {
        return "~/valkyrie.yaml";
    }

    static {
//        Constants.lookupTable = new InterpolatedMap();
//        Constants.lookupTable.put(1.0, 1.0);
//        Constants.lookupTable.put(50.0, 2500.0);
//        Constants.lookupTable.put(100.0, 10000.0);
        
        new Constants().readFromFile();
    }

    public static void main(String[] argv) {
      new Constants().writeToFile();

      System.out.println("driveRampRate: " + driveRampRate);
      System.out.println("encoderTicksPerRev: " + encoderTicksPerRev);
      System.out.println("minimumShiftDelay: " + minimumShiftDelay);
      System.out.println("lookupTable: " + lookupTable.toString());
    }
}
