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

import edu.wpi.first.wpilibj.command.ConditionalCommand;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.valkyrie.Robot;

/**
 *
 */
public class pegTester extends ConditionalCommand {


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public pegTester() {
	super(new DoNothing(), new RetryPeg());
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR

    }

    protected boolean condition() {
        DriveBackwardDistance.setDistance(-3);
        return Robot.gearDetector.pegPresent();
    }

    /* (non-Javadoc)
     * @see edu.wpi.first.wpilibj.command.ConditionalCommand#isFinished()
     */
    @Override
    protected boolean isFinished() {
        boolean done = super.isFinished();
        Logger.debug("pegTester isFinished: " + done);
        return done || Robot.gearDetector.pegPresent();
    }
    
    
}