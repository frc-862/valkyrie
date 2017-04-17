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
import org.usfirst.frc862.util.LightningMath;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveBackwardDistance extends Command {

    private static double distance = 2.2;
    
    public static void setDistance(double dist) {
        distance = dist;
    }
    
    private double m_distance;
    private double left_start;
    private double right_start;
    private double left_vel;
    private double right_vel;
    
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public DriveBackwardDistance(double distance) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_distance = distance;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    public DriveBackwardDistance() {
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        m_distance = DriveBackwardDistance.distance;
        Logger.debug("DriveDistance: " + m_distance);
        
        left_start = Robot.driveTrain.getLeftDistance();
        right_start = Robot.driveTrain.getRightDistance();
        
        if (Robot.inTeleop) {
            left_vel = -LightningMath.fps2rpm(Constants.autonSpeed) * 1.5;
        } else {
            left_vel = -LightningMath.fps2rpm(Constants.autonSpeed);            
        }
        
        right_vel = left_vel;    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.driveTrain.set(left_vel, right_vel);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Robot.driveTrain.getLeftDistance() - left_start) <= m_distance &&
                (Robot.driveTrain.getRightDistance() - right_start) <= m_distance;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.stop();
        Robot.core.orangeAndBlueLED();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
