package org.usfirst.frc862.trajectory;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Loop;
import org.usfirst.frc862.valkyrie.Robot;
import edu.wpi.first.wpilibj.Timer;

public class RobotStateEstimator implements Loop {
    private static RobotStateEstimator instance = new RobotStateEstimator();
    
    RobotState state = RobotState.getInstance();
    double left_encoder_prev_distance = 0;
    double right_encoder_prev_distance = 0;    

	public RobotStateEstimator() {
    }
	
	public RobotState getState() {
	    return state;
	}

    public static RobotStateEstimator getInstance() {
        return instance ;
    }

    @Override
    public void onStart() {
        left_encoder_prev_distance = Robot.driveTrain.getLeftDistance(); 
        right_encoder_prev_distance = Robot.driveTrain.getRightDistance();
    }

    @Override
    public void onLoop() {
        double time = Timer.getFPGATimestamp();
        double left_distance = Robot.driveTrain.getLeftDistance();
        double right_distance = Robot.driveTrain.getRightDistance();
        Rotation2d gyro_angle = Rotation2d.fromDegrees(Robot.driveTrain.getGyroAngle()); 
        RigidTransform2d odometry = state.generateOdometryFromSensors(
                left_distance - left_encoder_prev_distance,
                right_distance - right_encoder_prev_distance, gyro_angle);
        RigidTransform2d.Delta velocity = Kinematics.forwardKinematics(Robot.driveTrain.getLeftVelocity(),
                Robot.driveTrain.getRightVelocity());
        state.addObservations(time, odometry, velocity);
        left_encoder_prev_distance = left_distance;
        right_encoder_prev_distance = right_distance;
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub        
    }

    
//	public AddList<Watchable> getSubWatchables(AddList<Watchable> stem) {
//		return stem.put(new NumberInfo("x", state::getLatestX), new NumberInfo("y", state::getLatestY),
//				new NumberInfo("theta", state::getLatestTheta));
//	}

}
