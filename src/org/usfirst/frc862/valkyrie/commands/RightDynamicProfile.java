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
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;

import org.usfirst.frc862.util.CommandLogger;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryGenerator;
import com.team254.lib.trajectory.WaypointSequence;
import com.team254.lib.util.ChezyMath;
import com.team254.lib.trajectory.Trajectory.Pair;
import com.team254.lib.trajectory.TrajectoryFollower;

/**
 *
 */
public class RightDynamicProfile extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    private Trajectory trajectory;
    private int segment_index;
    private Pair path;
    private Notifier notifier;
    private TrajectoryFollower followerLeft = new TrajectoryFollower();
    private TrajectoryFollower followerRight = new TrajectoryFollower();
    private CommandLogger logger = new CommandLogger("extracheese");
    
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public RightDynamicProfile() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        logger.addDataElement("projected_left_pos");
        logger.addDataElement("requested_left_pos");
        logger.addDataElement("actual_left_pos");
        logger.addDataElement("projected_left_vel");
        logger.addDataElement("actual_left_vel");
        logger.addDataElement("projected_right_pos");
        logger.addDataElement("requested_right_pos");
        logger.addDataElement("actual_right_pos");
        logger.addDataElement("projected_right_vel");
        logger.addDataElement("actual_right_vel");
        logger.addDataElement("projected_heading");
        logger.addDataElement("actual_heading");
    }

    // Called just before this Command runs the first time
    protected void initialize() {        
        WaypointSequence points = new WaypointSequence(10);
        points.addWaypoint(new WaypointSequence.Waypoint(0,0,0)); //right side
        points.addWaypoint(new WaypointSequence.Waypoint(7.9, 2.7,Pathfinder.d2r(63))); //right side

        
        TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
        config.dt = 0.02;
        config.max_acc = 8 / 3;
        config.max_jerk = 50 / 2;
        config.max_vel = 3.5;
        trajectory = PathGenerator.generateFromPath(points, config);
        path = PathGenerator.makeLeftAndRightTrajectories(trajectory, Constants.wheelBase);
        segment_index = 0;
        
        Robot.driveTrain.resetDistance();
        
        followerLeft.configure(Constants.pathP, Constants.pathI, Constants.pathD, Constants.pathV, Constants.pathA);
        followerRight.configure(Constants.pathP, Constants.pathI, Constants.pathD, Constants.pathV, Constants.pathA);
        followerLeft.setTrajectory(path.left);
        followerRight.setTrajectory(path.right);
        followerLeft.reset();
        followerRight.reset();
        
        notifier = new Notifier(()-> followPath());
        notifier.startPeriodic(config.dt);
    }

    protected void followPath() {
        DriveTrain drive = Robot.driveTrain;
        double distanceL = drive.getLeftDistance();
        double distanceR = drive.getRightDistance();

        Trajectory.Segment left = followerLeft.getSegment();
        Trajectory.Segment right = followerRight.getSegment();
        
        double speedLeft = followerLeft.calculate(distanceL);
        double speedRight = followerRight.calculate(distanceR);
        
        double goalHeading = followerLeft.getHeading();
        double observedHeading = drive.getGyroAngleInRadians();

        double angleDiff = ChezyMath.getDifferenceInAngleDegrees(observedHeading, Pathfinder.r2d(goalHeading));

        double turn = Constants.pathTurn * angleDiff;
        double requestedLeft = speedLeft + turn;
        double requestedRight = speedRight - turn;
        drive.set(requestedLeft, requestedRight);

        logger.set("projected_left_pos", left.pos);
        logger.set("requested_left_pos", requestedLeft);
        logger.set("actual_left_pos", distanceL);
        logger.set("projected_left_vel", left.vel);
        logger.set("actual_left_vel", drive.getLeftVelocity());
        logger.set("projected_right_pos", right.pos);
        logger.set("requested_right_pos", requestedRight);
        logger.set("actual_right_pos", distanceR);
        logger.set("projected_right_vel", right.vel);
        logger.set("actual_right_vel", drive.getRightVelocity());
        logger.set("projected_heading", left.heading);
        logger.set("actual_heading", observedHeading);
        logger.write();
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        logger.drain();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return this.followerLeft.isFinishedTrajectory();
    }

    // Called once after isFinished returns true
    protected void end() {
        notifier.stop();
        logger.drain();
        logger.flush();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
