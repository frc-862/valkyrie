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
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import com.team254.lib.util.ChezyMath;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Rotate extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private double m_degrees;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private double goal;
    private double error;
    private float previousHeading;
    private double startTime;
    private double direction;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public Rotate(double degrees) {

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_degrees = degrees;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    public void setDesiredChange(double delta) {
        m_degrees = delta;
        updateSetPoint();
    }

    protected void updateSetPoint() {
        previousHeading = RobotMap.navx.getFusedHeading();
        Robot.driveTrain.setMode(DriveTrain.Modes.VELOCITY);
        goal = ChezyMath.boundAngleNeg180to180Degrees(previousHeading + m_degrees);        
        error = m_degrees;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        updateSetPoint();
        startTime = Timer.getFPGATimestamp();
        direction = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double heading = RobotMap.navx.getFusedHeading();
        error = ChezyMath.boundAngleNeg180to180Degrees(goal - heading);
        double rotatePGain = Robot.shifter.getMaxVelocity() / 180.0 * 3.9;
        double power = rotatePGain * error;

        if(Math.abs(power) < Constants.MinRotatePower){
            power = Constants.MinRotatePower * ((power > 0) ? 1 : -1);
        }

        double new_direction = Math.signum(power);
        if (!LightningMath.isEqual(direction, new_direction)) {
            direction = new_direction;
            startTime = Timer.getFPGATimestamp();
        } else {            
            double duration = Timer.getFPGATimestamp() - startTime;
            power += duration * Constants.rotateIGain;
        }
        
        Robot.driveTrain.set(power, -power);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(error) < Constants.rotateEpsilon;
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
