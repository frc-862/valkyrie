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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc862.trajectory.RobotState;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.vision.TargetInfo;

/**
 *
 */
public class VisionMonitor extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    private boolean found;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public VisionMonitor() {

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.vision);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        found = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        TargetInfo peg = RobotState.getInstance().getCurrentVisionTarget();
        if (peg != null) {
            double age = Timer.getFPGATimestamp() - peg.getCaptureTime();

            if (age > 0.5) {
                if (found) {
                    Robot.core.orangeAndBlueLED();
                } 
                found = false;
            } else if (!found && peg.getType() == 2) {
                Robot.core.blueLED();
                found = true;
            } else if (!found) {
                found = true;
                Robot.core.orangeLED();
            }
        } else if (found) {
            found = false;
            Robot.core.orangeAndBlueLED();            
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
