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
import org.usfirst.frc862.trajectory.RobotState;
import org.usfirst.frc862.util.LightningMath;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.StatefulCommand;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;
import org.usfirst.frc862.vision.TargetInfo;

import com.team254.lib.util.ChezyMath;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class AlignToAirShip extends StatefulCommand {

    boolean waiting_for_vision;
    private double start_time;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private double m_degrees;
    private double goal;
    private double error;
    private float previousHeading;
    private double distance;

    enum States { 
        WAIT_FOR_VISION,
        ROTATING,
        DONE
    }
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public AlignToAirShip() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        super(States.WAIT_FOR_VISION);
        
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    public void initialize() {
        setState(States.WAIT_FOR_VISION);
        Logger.debug("Initalize align to air ship");
        Robot.core.purpleLED();
        start_time = Timer.getFPGATimestamp() + 0.5;
    }
    
    public void waitForVisionEnter() {
        start_time = Timer.getFPGATimestamp() + 0.5;
    }
    
    public void waitForVision() {
        TargetInfo peg = RobotState.getInstance().getCurrentVisionTarget();
        if (peg != null && peg.getCaptureTime() > start_time) {
            double fudge = -25;
            fudge = 0;
            double lat = peg.getLatidunalDistance();
            double lon = peg.getLongitudinalDistance();
            if (Math.abs(lon) < 0.000001) {
                lon = 0.0001;
            }
            lat -= fudge;
            double theta = Math.toDegrees(Math.asin(lat / lon));
            theta += 2;

            setDesiredChange(theta);
            distance = Math.sqrt((peg.getLatidunalDistance() * peg.getLatidunalDistance()) + 
                    (peg.getLongitudinalDistance() * peg.getLongitudinalDistance()));
            DriveDistance.setDistance(LightningMath.mm2ft(distance) + 0.4);
            Logger.debug("  we have vision: " + m_degrees + " // " + error);
            
            setState(States.ROTATING);
        }
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

    public void rotating() {
        double heading = RobotMap.navx.getFusedHeading();
        error = ChezyMath.boundAngleNeg180to180Degrees(goal - heading);
        Logger.debug("  rotating: " + goal + " // " + heading + " // " + error);
        double rotatePGain = Robot.shifter.getMaxVelocity() / 180.0 * 3.9;
        double power = rotatePGain * error;

        if(Math.abs(power) < Constants.MinRotatePower){
            power = Constants.MinRotatePower * ((power > 0) ? 1 : -1);
        }

        if (Math.abs(error) < Constants.rotateEpsilon) {
            setState(States.DONE);
        }
        
        Robot.driveTrain.set(power, -power);
    }
    
    protected boolean isFinished() {
        return this.getState() == States.DONE;
    }
    
    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.stop();
        Logger.debug("rotated!");
        Logger.flush();        
        Robot.core.orangeAndBlueLED();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
