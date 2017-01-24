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

import com.kauailabs.navx.frc.AHRS;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;

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
    public static CANTalon driveTrainRightMotor1;
    public static CANTalon driveTrainRightMotor2;
    public static DoubleSolenoid driveTrainShifter;
    public static PowerDistributionPanel corePowerPanel;
    public static Compressor coreCompressor;
    public static SpeedController winchWinchMotor1;
    public static SpeedController winchWinchMotor2;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    
    public static AHRS navx;

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrainLeftMotor1 = new CANTalon(7);
        LiveWindow.addActuator("DriveTrain", "Left Motor 1", driveTrainLeftMotor1);
        
        driveTrainLeftMotor2 = new CANTalon(8);
        LiveWindow.addActuator("DriveTrain", "Left Motor 2", driveTrainLeftMotor2);
        
        driveTrainRightMotor1 = new CANTalon(5);
        LiveWindow.addActuator("DriveTrain", "Right Motor 1", driveTrainRightMotor1);
        
        driveTrainRightMotor2 = new CANTalon(6);
        LiveWindow.addActuator("DriveTrain", "Right Motor 2", driveTrainRightMotor2);
        
        driveTrainShifter = new DoubleSolenoid(11, 0, 1);
        LiveWindow.addActuator("DriveTrain", "Shifter", driveTrainShifter);
        
        corePowerPanel = new PowerDistributionPanel(1);
        LiveWindow.addSensor("Core", "PowerPanel", corePowerPanel);
        
        coreCompressor = new Compressor(11);
        
        
        winchWinchMotor1 = new VictorSP(0);
        LiveWindow.addActuator("Winch", "WinchMotor1", (VictorSP) winchWinchMotor1);
        
        winchWinchMotor2 = new VictorSP(1);
        LiveWindow.addActuator("Winch", "WinchMotor2", (VictorSP) winchWinchMotor2);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        navx = new AHRS(SerialPort.Port.kUSB, AHRS.SerialDataType.kProcessedData, (byte) 50);
        LiveWindow.addSensor("DriveTrain", "NavX", navx);
        
//        navx = new AHRS(I2C.Port.kMXP);
    }
}
