// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc862.valkyrie.commands;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;

/**
 *
 */
public class FollowWall extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public FollowWall() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    boolean wallIsLeft;
    
    // Called just before this Command runs the first time
    //HOW DO I MAKE THIS MORE NOTICABLE: Ultrasonic 1 is the left side when I'm writing this
    protected void initialize() {
    	Robot.driveTrain.set(Constants.WallFollowSpeedL, -Constants.WallFollowSpeedR);
    	if (Robot.driveTrain.getUltrasonicValue1() < Robot.driveTrain.getUltrasonicValue2()) {
    		wallIsLeft = true;
    	} else {
    		wallIsLeft = false;
    	}
    }
    
    double lSpeed = Constants.WallFollowSpeedL, rSpeed = Constants.WallFollowSpeedR;
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (wallIsLeft) {
    		if (Robot.driveTrain.getUltrasonicValue1() < Constants.WallFollowDistanceClose) {
    			lSpeed += .1;
    		}

    		if (Robot.driveTrain.getUltrasonicValue1() > Constants.WallFollowDistanceFar) {
    			lSpeed -= .1;
    		}
    		
    		if (Robot.driveTrain.getUltrasonicValue1()) {
    			
    		}
    	}
    	
    	if (!wallIsLeft) {
    		
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
