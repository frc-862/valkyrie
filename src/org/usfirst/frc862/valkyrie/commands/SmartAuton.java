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
import edu.wpi.first.wpilibj.command.StatefulCommand;
import org.usfirst.frc862.valkyrie.Robot;

/**
 *
 */
public class SmartAuton extends StatefulCommand {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    enum States { 
        VISION_ADJUST,
        DRIVE_UP,
        VERIFY_PEG,
        BACK_UP,
        DONE
    }
    boolean started;
    
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public SmartAuton() {
        super(States.VISION_ADJUST);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        
        registerState(States.VISION_ADJUST, new AlignToAirShip(), 
                () -> States.DRIVE_UP);
        registerState(States.DRIVE_UP, new DriveDistance(), 
                () -> States.VERIFY_PEG);
        registerState(States.BACK_UP, new DriveBackwardDistance(),
                () -> States.VISION_ADJUST);        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        this.setState(States.VISION_ADJUST);
        super.initialize();
    }

    public void verifyPeg() {
        if (Robot.gearDetector.pegPresent()) {
            setState(States.DONE);
        } else {
            setState(States.BACK_UP);            
        }
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.getState() == States.DONE;
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
