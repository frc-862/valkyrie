package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.MotionProfile;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import com.ctre.CANTalon;
import com.ctre.CANTalon.MotionProfileStatus;

import edu.wpi.first.wpilibj.Joystick;

public class MotionProfileMode extends SubsystemMode {

    private MotionProfileStatus status;

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Logger.debug("start motion profile");
        super.onStart();
        RobotMap.driveTrainLeftMotor1.clearMotionProfileTrajectories();
        CANTalon.TrajectoryPoint point = new CANTalon.TrajectoryPoint();
        for(int i=0; i<MotionProfile.kNumPoints; i++) {
            double[] profile = MotionProfile.Points[i];
            point.position = profile[0];
            point.velocity = profile[1];
            point.timeDurMs = (int) profile[2];
            point.profileSlotSelect = 0;
            point.velocityOnly = false;
            
            point.zeroPos = (i==0);
            
            point.isLastPoint = ((i + 1) == MotionProfile.kNumPoints);
            
            RobotMap.driveTrainLeftMotor1.pushMotionProfileTrajectory(point);
        }
        Logger.debug("sent point to talon");
        RobotMap.driveTrainLeftMotor1.changeControlMode(CANTalon.TalonControlMode.MotionProfile);
        Logger.debug("change talon mode");
    }

    @Override
    public void onLoop() {
        // TODO Auto-generated method stub
        super.onLoop();
        RobotMap.driveTrainLeftMotor1.getMotionProfileStatus(status);
        Logger.debug("Status: " + status.toString());

        RobotMap.driveTrainLeftMotor1.set(CANTalon.SetValueMotionProfile.Enable.value);
        RobotMap.driveTrainLeftMotor1.processMotionProfileBuffer();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }
}
