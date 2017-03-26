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
public class AlignToPeg extends Rotate {
    boolean waiting_for_vision;
    private double start_time;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public AlignToPeg() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        super(0);
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        waiting_for_vision = true;
        start_time = Timer.getFPGATimestamp();
    }

    protected void execute() {
        if (waiting_for_vision) {
            TargetInfo peg = RobotState.getInstance().getCurrentVisionTarget();
            if (peg.getCaptureTime() > start_time) {
                waiting_for_vision = false;
                setDesiredChange(peg.getTheta());
            }
        } else {
            super.execute();
        }
    }
    
    protected boolean isFinished() {
        return !waiting_for_vision && super.isFinished();
    }
}
