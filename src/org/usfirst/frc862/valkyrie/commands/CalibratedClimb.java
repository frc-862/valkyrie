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
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc862.util.CommandLogger;
import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;

/**
 *
 */
public class CalibratedClimb extends Command {
    private PowerDistributionPanel pdp;
    private CommandLogger logger;
    private double power;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public CalibratedClimb() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.winch);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        logger = new CommandLogger(this.getName());
        logger.addDataElement("winch_1_current");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        power = SmartDashboard.getNumber("Climb Power", 0.862);
        Robot.winch.climb(power);
        pdp = Robot.core.getPDP();
        
//        DataLogger.addDataElement("Winch Power 1", () -> powerPanel.getCurrent(Constants.winch1PowerChannel));
//        DataLogger.addDataElement("Winch Power 2", () -> powerPanel.getCurrent(Constants.winch2PowerChannel));
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (totalWinchCurrent() > Constants.slowWinchCurrent) {
            power = SmartDashboard.getNumber("Climb Power", 0.862) / 2;
        }
        Robot.winch.climb(power);
    }

    protected double totalWinchCurrent() {
        return pdp.getCurrent(Constants.winch1PowerChannel) +
                pdp.getCurrent(Constants.winch2PowerChannel);
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return totalWinchCurrent() > Constants.stopWinchCurrent;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.winch.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
