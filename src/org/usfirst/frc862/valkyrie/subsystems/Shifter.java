// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc862.valkyrie.subsystems;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.commands.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shifter extends Subsystem {

    private int gear_state = 0;
    private double lastShift = 0;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final DoubleSolenoid shifter = RobotMap.shifterShifter;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        setDefaultCommand(new Autoshifter());
    }

    public void useAutoShifter() {
        
    }
    
    public void useManualShifter() {
        
    }
    
    public void downShift() {
        if (!isLowGear()) {
            shifter.set(Value.kReverse);
            Robot.core.useAirUnits(Constants.shiftAirUnit);
            gear_state = 1;
            lastShift = Timer.getFPGATimestamp();
            Robot.driveTrain.configureLowGear();
        }
    }

    public void upShift() {
        if (!isHighGear()) {
            shifter.set(Value.kForward);
            Robot.core.useAirUnits(Constants.shiftAirUnit);
            gear_state = 2;
            lastShift = Timer.getFPGATimestamp();
            Robot.driveTrain.configureHighGear();
        }
    }

    public boolean isLowGear() {
        return gear_state == 1;
    }

    public boolean isHighGear() {
        return gear_state == 2;
    }
    
    public double getLastShiftTime() {
        return lastShift;
    }

    public boolean hysteresisSafetyNet() {
        return (Timer.getFPGATimestamp() - lastShift) > Constants.shiftHysteresis;
    }
    
    public double getMaxVelocity(){
        if(isLowGear())
        {
            return Constants.maxVelocityLow; 
        }
        else 
        {
            return Constants.maxVelocityHigh;
        }
    }
    
}


