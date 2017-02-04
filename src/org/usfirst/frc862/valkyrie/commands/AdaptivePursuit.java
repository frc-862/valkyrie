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

import org.usfirst.frc862.util.LightningKinematics;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain.Modes;

import com.team254.lib.util.AdaptivePurePursuitController;
import com.team254.lib.util.Path;
import com.team254.lib.util.RigidTransform2d;

/**
 *
 */
public class AdaptivePursuit extends Command {

    private DriveTrain mDrive = Robot.driveTrain;

    private Path mPath;
    private boolean mReversed;
    private boolean mHasStarted;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    private AdaptivePurePursuitController pathFollowingController_;
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public AdaptivePursuit(Path path, boolean reversed) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORPath path, boolean reversed) {
        mPath = path;
        mReversed = reversed;
        mHasStarted = false;
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        pathFollowingController_ = new AdaptivePurePursuitController(Constants.kPathFollowingLookahead,
                Constants.kPathFollowingMaxAccel, Constants.kLooperDt, path, reversed, 0.25);

    }
    
    

    // Called just before this Command runs the first time
    protected void initialize() {
        mDrive.setMode(Modes.VELOCITY);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//        RigidTransform2d robot_pose = RobotState.getInstance().getLatestFieldToVehicle().getValue();
//        RigidTransform2d.Delta command = pathFollowingController_.update(robot_pose, Timer.getFPGATimestamp());
//        LightningKinematics.DriveVelocity setpoint = LightningKinematics.inverseKinematics(command);
//
//        // Scale the command to respect the max velocity limits
//        double max_vel = 0.0;
//        max_vel = Math.max(max_vel, Math.abs(setpoint.left));
//        max_vel = Math.max(max_vel, Math.abs(setpoint.right));
//        if (max_vel > Constants.kPathFollowingMaxVel) {
//            double scaling = Constants.kPathFollowingMaxVel / max_vel;
//            setpoint = new LightningKinematics.DriveVelocity(setpoint.left * scaling, setpoint.right * scaling);
//        }
//        mDrive.set(setpoint.left, setpoint.right);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean done = pathFollowingController_.isDone();
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
        mDrive.stop();
    }


    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
