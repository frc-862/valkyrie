// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc862.valkyrie;

import org.usfirst.frc862.util.DynamicPathCommand;
import org.usfirst.frc862.util.FaultCode;
import org.usfirst.frc862.util.Logger;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C.Port;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import org.usfirst.frc862.util.DynamicPathCommand;
import org.usfirst.frc862.util.StatefulCommand;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static CANTalon driveTrainLeftMotor1;
    public static CANTalon driveTrainLeftMotor2;
    public static CANTalon driveTrainLeftMotor3;
    public static CANTalon driveTrainRightMotor1;
    public static CANTalon driveTrainRightMotor2;
    public static CANTalon driveTrainRightMotor3;
    public static AnalogInput coreFrontUltrasonic;
    public static AnalogInput coreLeftUltrasonic;
    public static AnalogInput coreRightUltrasonic;
    public static AnalogInput coreBackUltrasonic;
    public static PowerDistributionPanel corePowerPanel;
    public static Compressor coreCompressor;
    public static Solenoid coreLEDRing;
    public static SpeedController winchWinchMotor1;
    public static SpeedController winchWinchMotor2;
    public static DigitalInput winchClimbTouchpad;
    public static DoubleSolenoid shifterShifter;
    public static DoubleSolenoid gearCollectorcollector;
    public static DoubleSolenoid gearCollectorGearEject;
    public static DigitalInput gearDetectorGearSensor;
    public static DynamicPathCommand autonBlueBoiler;
    public static DynamicPathCommand autonBlueFeeder;
    public static DynamicPathCommand autonRedBoiler;
    public static DynamicPathCommand autonRedFeeder;
    public static DynamicPathCommand autonStraight;
    public static DynamicPathCommand driveStraightDownField;
    public static DynamicPathCommand driveDownFieldVeerLeft;
    public static DynamicPathCommand driveDownFieldVeerRight;
    public static StatefulCommand alignToAirShip;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    
    public static AHRS navx;

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrainLeftMotor1 = new CANTalon(1);
        LiveWindow.addActuator("DriveTrain", "Left Motor 1", driveTrainLeftMotor1);
        
        driveTrainLeftMotor2 = new CANTalon(2);
        LiveWindow.addActuator("DriveTrain", "Left Motor 2", driveTrainLeftMotor2);
        
        driveTrainLeftMotor3 = new CANTalon(3);
        LiveWindow.addActuator("DriveTrain", "Left Motor 3", driveTrainLeftMotor3);
        
        driveTrainRightMotor1 = new CANTalon(4);
        LiveWindow.addActuator("DriveTrain", "Right Motor 1", driveTrainRightMotor1);
        
        driveTrainRightMotor2 = new CANTalon(5);
        LiveWindow.addActuator("DriveTrain", "Right Motor 2", driveTrainRightMotor2);
        
        driveTrainRightMotor3 = new CANTalon(6);
        LiveWindow.addActuator("DriveTrain", "Right Motor 3", driveTrainRightMotor3);
        
        coreFrontUltrasonic = new AnalogInput(2);
        LiveWindow.addSensor("Core", "Front Ultrasonic", coreFrontUltrasonic);
        
        coreLeftUltrasonic = new AnalogInput(1);
        LiveWindow.addSensor("Core", "Left Ultrasonic", coreLeftUltrasonic);
        
        coreRightUltrasonic = new AnalogInput(0);
        LiveWindow.addSensor("Core", "Right Ultrasonic", coreRightUltrasonic);
        
        coreBackUltrasonic = new AnalogInput(3);
        LiveWindow.addSensor("Core", "Back Ultrasonic", coreBackUltrasonic);
        
        corePowerPanel = new PowerDistributionPanel(10);
        LiveWindow.addSensor("Core", "PowerPanel", corePowerPanel);
        
        coreCompressor = new Compressor(11);
        
        
        coreLEDRing = new Solenoid(11, 6);
        LiveWindow.addActuator("Core", "LED Ring", coreLEDRing);
        
        winchWinchMotor1 = new VictorSP(0);
        LiveWindow.addActuator("Winch", "WinchMotor1", (VictorSP) winchWinchMotor1);
        
        winchWinchMotor2 = new VictorSP(1);
        LiveWindow.addActuator("Winch", "WinchMotor2", (VictorSP) winchWinchMotor2);
        
        winchClimbTouchpad = new DigitalInput(1);
        LiveWindow.addSensor("Winch", "Climb Touchpad", winchClimbTouchpad);
        
        shifterShifter = new DoubleSolenoid(11, 0, 1);
        LiveWindow.addActuator("Shifter", "Shifter", shifterShifter);
        
        gearCollectorcollector = new DoubleSolenoid(11, 4, 5);
        LiveWindow.addActuator("Gear Collector", "collector", gearCollectorcollector);
        
        gearCollectorGearEject = new DoubleSolenoid(11, 2, 3);
        LiveWindow.addActuator("Gear Collector", "Gear Eject", gearCollectorGearEject);
        
        gearDetectorGearSensor = new DigitalInput(0);
        LiveWindow.addSensor("GearDetector", "Gear Sensor", gearDetectorGearSensor);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        //navx = new AHRS(Port.kUSB, AHRS.SerialDataType.kProcessedData, (byte) 50);
        navx = new AHRS(Port.kOnboard, (byte) 50);
        Logger.debug("Navx: " + navx);
        
        if (navx == null) {
            FaultCode.write(FaultCode.Codes.NAVX_ERROR);
        } else {
            for (int i = 0; i < 10 && !navx.isConnected(); ++i) {
                try {
                    navx.reset();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            
            if (navx.isConnected()) {
                Logger.debug("We have a connected navx");
                LiveWindow.addSensor("DriveTrain", "NavX", navx);
            } else {
                Logger.error("Failed to connect navx");
                FaultCode.write(FaultCode.Codes.NAVX_ERROR, "Not connected - sad");
            }
            Logger.flush();
        }
        
//        navx = new AHRS(I2C.Port.kMXP);
    }
    
    public static void resetNavX() {
        if (navx == null) {
            //navx = new AHRS(Port.kUSB, AHRS.SerialDataType.kProcessedData, (byte) 50);
        	navx = new AHRS(Port.kOnboard, (byte) 50);
        }
        Logger.debug("Navx: " + navx);
        
        if (navx == null) {
            FaultCode.write(FaultCode.Codes.NAVX_ERROR);
        } else {
            for (int i = 0; i < 10 && !navx.isConnected(); ++i) {
                try {
                    navx.reset();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            
            if (navx.isConnected()) {
                Logger.debug("We have a connected navx");
                LiveWindow.addSensor("DriveTrain", "NavX", navx);
            } else {
                Logger.error("Failed to connect navx");
                FaultCode.write(FaultCode.Codes.NAVX_ERROR, "Not connected - sad");
            }
            Logger.flush();
        }        
    }
}
