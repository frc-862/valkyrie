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
import org.usfirst.frc862.util.LightningTimer;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.GearCollector;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ToggleGearEject extends Command {
    enum State {
        OPENING_COLLECTOR,
        OPENING_GEAR,
        CLOSING_COLLECTOR,
        CLOSING_GEAR,
        BACKING_AWAY,
        FINISHED,
        DONE
    }
    State state;
    
    private LightningTimer timer = new LightningTimer();

    private double timeout;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public ToggleGearEject() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.gearCollector);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        Robot.gearCollector.toggleGearEject();
        if (Robot.gearCollector.getEjectDoorState() == GearCollector.State.EXTENDED) {
            state = State.CLOSING_GEAR;
        } else {
            state = State.CLOSING_COLLECTOR;
        }
        timeout = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!timer.hasPeriodPassed(timeout)) {
            return;
        }
        
        switch (state) {
        case CLOSING_COLLECTOR:
            Robot.gearCollector.retract();
            state = State.OPENING_GEAR;
            timer.reset();
            timeout = 0.2;
            break;
        case CLOSING_GEAR:
            Robot.gearCollector.closeGearEjectDoor();
            timer.reset();
            timeout = 0.2;
            state = State.OPENING_COLLECTOR;
            break;
        case DONE:
            Robot.driveTrain.stop();
            break;
        case OPENING_COLLECTOR:
            Robot.gearCollector.extend();
            state = State.DONE;
            break;
        case OPENING_GEAR:
            Robot.gearCollector.ejectGear();
            state = State.BACKING_AWAY;
            break;
        case BACKING_AWAY:
            timeout = Constants.backupDuration;
            timer.reset();
            Robot.driveTrain.set(Constants.backupPower, Constants.backupPower);
            state = State.FINISHED;
            break;
            
            // required to keep isFinished from triggering 
            // while we are still in a timeout state (BACKING_AWAY)
        case FINISHED:
            state = State.DONE;
            break;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return state == State.DONE;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
