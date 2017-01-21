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

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.MotionProfile;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

/**
 *
 */
public class FollowMotionProfile extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public FollowMotionProfile() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Logger.debug("Set follow mode");
        Robot.driveTrain.motionProfileMode.setPoints(MotionProfile.Points, MotionProfile.Points);
        Robot.driveTrain.setMode(DriveTrain.Modes.MOTION_PROFILE);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.motionProfileMode.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
        // TODO mode reset should be a call to drivetrain,
        // in case we have reset to OPEN_LOOP, due to a sensor
        // fault
        Robot.driveTrain.setMode(DriveTrain.Modes.VELOCITY);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
