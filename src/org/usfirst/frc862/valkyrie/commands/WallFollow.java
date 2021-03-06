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
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.Core;
import org.usfirst.frc862.valkyrie.subsystems.Core.Ultrasonic;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class WallFollow extends Command {

    private Core.Ultrasonic sensor;
    private double direction;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public WallFollow() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        sensor = Ultrasonic.Left;
        direction = 1.0;
        Alliance alliance = DriverStation.getInstance().getAlliance();
        
        if(alliance == Alliance.Red && Math.abs(Robot.driveTrain.getGyroAngle()) > 100){
            sensor = Ultrasonic.Right;
            direction = -1;
        }
        if(alliance == Alliance.Blue && Math.abs(Robot.driveTrain.getGyroAngle()) < 80){
            sensor = Ultrasonic.Right;
            direction = -1;
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double distance = Robot.core.getUltrasonic(sensor);
        double error = Constants.WallFollowDistance - distance;
        Robot.driveTrain.set(Constants.WallFollowSpeed + (error * Constants.WallFollowP * direction), 
                Constants.WallFollowSpeed - (error * Constants.WallFollowP * direction));
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
        end();
    }
}
