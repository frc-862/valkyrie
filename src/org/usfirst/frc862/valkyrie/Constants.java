package org.usfirst.frc862.valkyrie;

import java.io.File;

import org.usfirst.frc862.util.ConstantsBase;
import org.usfirst.frc862.util.InterpolatedMap;
import org.usfirst.frc862.util.LightningMath;

@SuppressWarnings("WeakerAccess")
public class Constants extends ConstantsBase {
	
    
    // Note this is final, it will not be
    // in the config file, and if you put
    // it there, the value will be ignored
    // as the value here in the code is 
    // immutable
    
    
	//Autoshifting
    public static double HCScaryTriggerTime = 1.5;
    public static double CrashDuration = 0.1;
    public static double LostBattleTriggerTime = 0.75;
    public static double CompressorPauseTime = 1;
    public static double HCTriggerTime = 0.25;
    public static double HighCurrentThreshold = 200;
    public static double LowVoltage = 10;
    public static double CrashDeacceleration = -200;
    public static double CoastTriggerTime = 1.0;
    public static double CoastVelocity = 200;
    public static double VelocityUpshiftTime = 0.1;
    public static double MinRequestedPowerForUpshift = 0.75;
    public static double LVTriggerTime = 0.5;
    public static double MaxCoastPower = 0.35;
    public static double MinUpshiftVelocity = 300;
    public static double shiftHysteresis = 0.5;
    public static double shiftDelay = 0.25;
    public static double minimumShiftDelay = 0.75;
    public static double maxAirUnits = 20; //amount of air in tank
    public static double shiftAirUnit = 1; //amount of air used per actuation of the shifter
    public static double minimumAirReserve = 3;
    public static double compressorDisabledDelay = 0.5;
    public static double seriousCurrentDrawTimer = 0.15;

    
    //Robot Physical Specs
    public static double wheelBase = 22.0 / 12.0;  // in feet
    public static double WheelDiameter = 4.0 / 12.0;  // in feet
    public static double WheelCircumference = WheelDiameter * Math.PI;  // in feet
    
    
    //Joystick
    public static double deadband = 0.00;

    
    //DriveTrain
    public static double driveTrainLoopRate = 0.10;
    public static double maxRampRate = 0.75;
    public static double dataLoggerPeriod = 0.05;  // 20 times a second by default
    public static double maxVelocityLow = 350;
    public static double maxVelocityHigh = 950;
    public static double driveRampRate = 300;
    public static int encoderTicksPerRev = 360;
    public static double velocityFeedForwardRLow = 4.5 / 4;
    public static double velocityFeedForwardLLow = 4.5 / 4;
    public static double velocityPTermLow = 0.64;
    public static double velocityPTermHigh = 0.2;
    public static double velocityFeedForwardRHigh = 1.62/4;
    public static double velocityFeedForwardLHigh = 1.62/4;
	public static double MotorOffTime = 1.0;
    public static double rotateEpsilon = 2.5;
    public static double straightCommandDelta = 0.19;
    public static double straightenPGain = 1 / 90.0;


	//Brake mode (not in use)
    public static double brakeP = 0.5;
    public static double brakeI = 0;
    public static double brakeD = 0;
    public static double brakeF = 0;
    public static int brakeRampRate = 0;
    public static double brakeIZone = 0;
    public static int brakeSlot = 1;
    
	
	//Logging
    public static int logDepth = 1000;
    public static double backgroundLoopRate = 0.5;
    
    
    //Mechanisms
    public static double winchRampTime = 3;
    

    //Motion Profile
    public static double pathP = LightningMath.fps2rpm(1.5);
    public static double pathI = 0;
    public static double pathD = 0;
    public static double pathV = LightningMath.fps2rpm(1);
    public static double pathA = 0; //pathV / 2;
    public static double pathTurn = -3.0 / 80;  // borrowed from Poofs :)
    public static double Path_dt = 0.02;
    public static double Path_max_acc = 8/3;
    public static double Path_max_jerk = 25;
    public static double Path_max_vel = 3.5;
    //public static InterpolatedMap lookupTable = new InterpolatedMap(new Double[]{1.0,2.1,3.2,4.3}); //interpolation example

    
    //vision
	public static double DMPKP = 0.0;
	public static double DMPKI = 0.0;
	public static double DMPKD = 0.0;
	public static double DMPKV = 300;
	public static double DMPKA = 0.0;
    public static int kCameraPitchAngleDegrees = 0;
    public static int kCameraYawAngleDegrees = 0;
    public static int kCenterOfTargetHeight = 0;
    public static int kCameraZOffset = 0;
    public static double kTrackScrubFactor = 1.0;
    public static double kTrackEffectiveDiameter = 4.0;
    public static double kPathFollowingMaxAccel = 3.6;
    public static double kLooperDt = 0.05;
    public static double kPathFollowingLookahead = 3.0;
    public static double kPathFollowingMaxVel = 16.0;
    public static double kDriveBaseLockKp = 0.5;
    public static double kDriveBaseLockKi = 0;
    public static double kDriveBaseLockKd = 0;
    public static double kDriveBaseLockKf = 0;
    public static int kDriveBaseLockIZone = 0;
    public static double kDriveBaseLockRampRate = 0;
    public static int kDriveBaseLockAllowableError = 10;
    

    
    public String getFileName() {
        return "~/valkyrie.yaml";
    }

    static {
//        Constants.lookupTable = new InterpolatedMap();
//        Constants.lookupTable.put(1.0, 1.0);
//        Constants.lookupTable.put(50.0, 2500.0);
//        Constants.lookupTable.put(100.0, 10000.0);
        
        File file = new File("/home/lvuser/obot");
        if (file.exists()) {
            // TODO any obot specific over-rides can go here
        }

        // new Constants().readFromFile();
    }
}
