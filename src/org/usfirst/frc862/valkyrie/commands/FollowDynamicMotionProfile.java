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
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain.Modes;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.*;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

/**
 *
 */
public class FollowDynamicMotionProfile extends Command {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	public static Trajectory lightningTrajectory;
	private EncoderFollower left, right;
	private int index;
	private TankModifier tank;
	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public FollowDynamicMotionProfile() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Waypoint[] points = new Waypoint[] {
    			//new Waypoint(getDynamicXValue(), getDynamicYValue(), getDynamicAngle()),
    			new Waypoint(-2, -2, 0),
    			new Waypoint(0, 0, 0)
    	};
    	
    	Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH,
    			Constants.MotionProfileLoopSpeed, Constants.MotionProfileMaxVelocity,
    			Constants.MotionProfileMaxAcceleration, Constants.MotionProfileMaxJerk);
    	Trajectory trajectory = Pathfinder.generate(points, config);
    	TankModifier tank = new TankModifier(trajectory).modify(0.5);
    	left = new EncoderFollower(tank.getLeftTrajectory());
    	right = new EncoderFollower(tank.getRightTrajectory());
    	left.configureEncoder(Robot.driveTrain.getLeftEncoder(), 360, Constants.WheelDiameter);
    	right.configureEncoder(Robot.driveTrain.getRightEncoder(), 360, Constants.WheelDiameter);
    	left.configurePIDVA(Constants.DMPKP, Constants.DMPKI, Constants.DMPKD, Constants.DMPKV, Constants.DMPKA);
    	right.configurePIDVA(Constants.DMPKP, Constants.DMPKI, Constants.DMPKD, Constants.DMPKV, Constants.DMPKA);
    	
    	index = 0; 
    	Robot.driveTrain.setMode(Modes.OPEN_LOOP);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double l = left.calculate(Robot.driveTrain.getLeftEncoder());
    	double r = right.calculate(Robot.driveTrain.getRightEncoder());
    	
    	double gyro_heading = RobotMap.navx.getFusedHeading();
    	double desired_heading = left.getHeading();
    	
    	double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
    	double turn = 0.8 * (-1/80.0) * angleDifference;

    	Robot.driveTrain.set(l + turn, r - turn);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//return index >= left.length();
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
//    @SuppressWarnings("deprecation")
//	public static double getDynamicXValue() {
//    	return SmartDashboard.getNumber("Dynamic Motion Profile X");
//    } 
//    
//    @SuppressWarnings("deprecation")
//	public static double getDynamicYValue() {
//    	return SmartDashboard.getNumber("Dynamic Motion Profile y");
//    }   
//    
//    @SuppressWarnings("deprecation")
//	public static double getDynamicAngle() {
//    	return SmartDashboard.getNumber("Dynamic Motion Profile Angle");
//    }
}
