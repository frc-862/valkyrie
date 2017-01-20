package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.MotionProfile;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import com.ctre.CANTalon;
import com.ctre.CANTalon.MotionProfileStatus;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class MotionProfileMode extends SubsystemMode {

    private MotionProfileStatus status = new MotionProfileStatus();

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Logger.debug("start motion profile");
        super.onStart();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> {
           t.clearMotionProfileTrajectories(); 
           t.changeMotionControlFramePeriod(5);
           t.changeControlMode(CANTalon.TalonControlMode.MotionProfile);
           t.set(CANTalon.SetValueMotionProfile.Disable.value);
        });

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
        Logger.debug("sent points to talon");
        
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> { 
  //          t.setPID(0.1, 0, 0);
            t.setPID(0.1, 0, 0, 3.41 / 4, 0, 0, 0);
//            t.setF(3.41 / 4);           
        });
    }

    @Override
    public void onLoop() {
        // TODO Auto-generated method stub
        super.onLoop();

        RobotMap.driveTrainLeftMotor1.set(CANTalon.SetValueMotionProfile.Enable.value);
        RobotMap.driveTrainLeftMotor1.processMotionProfileBuffer();
        RobotMap.driveTrainLeftMotor1.getMotionProfileStatus(status);
        Logger.debug("onLoop Status: " + status.toString());
        Logger.debug("valid point: " + status.activePointValid);
        Logger.debug("position: " + status.activePoint.position);        
        Logger.debug("velocity: " + status.activePoint.velocity);        
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        Logger.debug("stopping motion profile");
        super.onStop();
    }
}
