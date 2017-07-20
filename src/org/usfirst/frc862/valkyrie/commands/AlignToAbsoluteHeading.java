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
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import com.team254.lib.util.ChezyMath;

/**
 *
 */
public class AlignToAbsoluteHeading extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private double m_heading;
    private double goal;
    private double error;
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public AlignToAbsoluteHeading(double heading) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        m_heading = heading;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        goal = m_heading;
    }

    public void setDesiredPosition(double goal) {
        this.goal = ChezyMath.boundAngleNeg180to180Degrees(goal);        
        updateSetPoint();
    }
    
    protected void updateSetPoint() {
        double heading = RobotMap.navx.getFusedHeading();
        error = ChezyMath.boundAngleNeg180to180Degrees(goal - heading);
    }
    
    // Called just before this Command runs the first time
    public void initialize() {
        Robot.driveTrain.setMode(DriveTrain.Modes.VELOCITY);
        updateSetPoint();
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
        double heading = RobotMap.navx.getFusedHeading();
        error = ChezyMath.boundAngleNeg180to180Degrees(goal - heading);
        double rotatePGain = Robot.shifter.getMaxVelocity() / 180.0 * 3.9;
        double power = rotatePGain * error;
        
        if(Math.abs(power) < Constants.MinRotatePower){
            power = Constants.MinRotatePower * ((power > 0) ? 1 : -1);
        }
        Robot.driveTrain.set(power, -power);
    }

    // Make this return true when this Command no longer needs to run execute()
    public boolean isFinished() {
        return Math.abs(error) < Constants.rotateEpsilon;
    }

    // Called once after isFinished returns true
    public void end() {
        Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    public void interrupted() {
        end();
    }
}
