package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.util.FaultCode;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.LoggerValue;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;
import com.ctre.CANTalon;
import com.ctre.CANTalon.MotionProfileStatus;
import edu.wpi.first.wpilibj.Notifier;

public class MotionProfileMode extends SubsystemMode implements Runnable {
    private Notifier pump;
    private double[][] leftPoints = null;
    private double[][] rightPoints = null;
    private MotionProfileStatus status = new MotionProfileStatus();
    private boolean done = false;

    static private LoggerValue mpmSetPoint = new LoggerValue();;
    static private LoggerValue mpmSetEnc = new LoggerValue();;

    static {
        DataLogger.addDataElement("mpmSetPoint", mpmSetPoint);
        DataLogger.addDataElement("mpmSetEnc", mpmSetEnc);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        done = false;
        Logger.debug("start motion profile");
        super.onStart();        
        CANTalon.TrajectoryPoint point = new CANTalon.TrajectoryPoint();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> {
           t.clearMotionProfileTrajectories(); 
           t.changeMotionControlFramePeriod(5);
           t.changeControlMode(CANTalon.TalonControlMode.MotionProfile);
           t.set(CANTalon.SetValueMotionProfile.Disable.value);
           t.setPID(Constants.velocityPTerm, 0, 0, 
                   Constants.velocityFeedForward, 0, 0, 0);
        });

        int period = (int) leftPoints[0][2];
        final int pointCount = Math.min(leftPoints.length, rightPoints.length);
        for(int i=0; i < pointCount; ++i) {
            double[] profile = leftPoints[i];
            point.position = profile[0];
            point.velocity = profile[1];
            point.timeDurMs = (int) profile[2];
            period = Math.min(point.timeDurMs, period);
            point.profileSlotSelect = 0;
            point.velocityOnly = false;
            
            point.zeroPos = (i==0);
            point.isLastPoint = ((i + 1) == pointCount);

            RobotMap.driveTrainLeftMotor1.pushMotionProfileTrajectory(point);
            
            profile = rightPoints[i];
            point.position = profile[0];
            point.velocity = profile[1];
            point.timeDurMs = (int) profile[2];
            period = Math.min(point.timeDurMs, period);
            point.profileSlotSelect = 0;
            point.velocityOnly = false;
            
            point.zeroPos = (i==0);
            point.isLastPoint = ((i + 1) == pointCount);

            RobotMap.driveTrainRightMotor1.pushMotionProfileTrajectory(point);
        }
        Logger.debug("sent points to talon"); 
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> {
            t.set(CANTalon.SetValueMotionProfile.Enable.value);
         });

        pump = new Notifier(this);
        if (period <= 0) period = 10;
        pump.startPeriodic(0.0005 * period);
    }

    public void run() {
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> {
           t.processMotionProfileBuffer(); 
        });
    }

    public void onLoop() {
        // TODO Auto-generated method stub
        super.onLoop();

        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> {
          t.getMotionProfileStatus(status);
          if (status.activePointValid && status.activePoint.isLastPoint) {
            // stop here
            t.set(CANTalon.SetValueMotionProfile.Hold.value);
            done = true;
          } else {
              MotionProfileMode.mpmSetPoint.set(status.activePoint.velocity);
              MotionProfileMode.mpmSetEnc.set(status.activePoint.position);
            t.set(CANTalon.SetValueMotionProfile.Enable.value);
          }
        });
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        Logger.debug("stopping motion profile");
        pump.stop();
        super.onStop();
    }

    public void setPoints(double[][] left, double[][] right) {
        leftPoints = left;
        rightPoints = right;
        
        if (leftPoints.length != rightPoints.length) {
            FaultCode.write(FaultCode.Codes.MISMATCHED_MOTION_PROFILES);
        }
    }
    
    public boolean isFinished() {
        return done;
    }
}
