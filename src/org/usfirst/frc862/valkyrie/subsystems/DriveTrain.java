package org.usfirst.frc862.valkyrie.subsystems;

import java.util.function.Consumer;

import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.util.FaultCode;
import org.usfirst.frc862.util.JoystickFilter;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Loop;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.commands.TeleopVelocityDrive;
import org.usfirst.frc862.valkyrie.subsystems.modes.AdaptivePursuitMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.BrakeMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.EncoderMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.HeadingMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.MotionProfileMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.OpenLoopMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.SubsystemMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.VelocityMode;

import com.ctre.CANTalon;
import com.ctre.CANTalon.MotionProfileStatus;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import static org.usfirst.frc862.util.LightningMath.*;

/**
 *
 */
@SuppressWarnings("unused")
public class DriveTrain extends Subsystem implements Loop {

    public enum Modes {
        OPEN_LOOP, VELOCITY, BRAKE, HEADING, MOTION_PROFILE, ADAPTIVE_PURSUIT, ENCODER
    }    
    
    private final Object modeRunningLock = new Object();
    private Modes mode;
    OpenLoopMode openLoopMode;
    VelocityMode velocityMode;
    BrakeMode brakeMode;
    HeadingMode headingMode;
    AdaptivePursuitMode adaptivePursuitMode;
    public MotionProfileMode motionProfileMode;
    SubsystemMode currentMode;
    protected double driveTheta;
    private boolean drivingStraight;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon leftMotor1 = RobotMap.driveTrainLeftMotor1;
    private final CANTalon leftMotor2 = RobotMap.driveTrainLeftMotor2;
    private final CANTalon leftMotor3 = RobotMap.driveTrainLeftMotor3;
    private final CANTalon rightMotor1 = RobotMap.driveTrainRightMotor1;
    private final CANTalon rightMotor2 = RobotMap.driveTrainRightMotor2;
    private final CANTalon rightMotor3 = RobotMap.driveTrainRightMotor3;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final AHRS navx = RobotMap.navx;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    private boolean running = false;
    private JoystickFilter filter = new JoystickFilter(Constants.deadband, 0, 1, JoystickFilter.Mode.SQUARED);
    private MotionProfileStatus status = new MotionProfileStatus();
    private double leftRequestedPower;
    private double rightRequestedPower;
    private EncoderMode encoderMode;

    // private double start;
    // private double stop;
    
    public DriveTrain() {
        initialize();
    }
    
    private void initialize() {
        leftMotor1.reverseSensor(false);
        leftMotor1.reverseOutput(true);
        leftMotor1.setVoltageRampRate(Constants.driveRampRate);
        
        // Followers should only be reversed if you want them to run opposite of the
        // master controller
        //leftMotor2.reverseOutput(false);        
        if (leftMotor1.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) == 
                CANTalon.FeedbackDeviceStatus.FeedbackStatusNotPresent) {
            FaultCode.write(FaultCode.Codes.LEFT_ENCODER_NOT_FOUND);
        } else {
            leftMotor1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
            leftMotor1.setEncPosition(0);
            leftMotor1.configEncoderCodesPerRev(360);
            DataLogger.addDataElement("left encoder pos", () -> leftMotor1.getPosition());
            DataLogger.addDataElement("left encoder vel", () -> leftMotor1.getSpeed());
            DataLogger.addDataElement("left closed loop error", () -> leftMotor1.getError());
            DataLogger.addDataElement("left output voltage", () -> leftMotor1.getOutputVoltage());
            DataLogger.addDataElement("left set point", () -> leftMotor1.getSetpoint());
        }
        
        rightMotor1.reverseSensor(false);
        rightMotor1.reverseOutput(false);
        // Followers should only be reversed if you want them to run opposite of the
        // master controller
        rightMotor2.reverseOutput(false);        
        if (rightMotor1.isSensorPresent(CANTalon.FeedbackDevice.QuadEncoder) ==
                CANTalon.FeedbackDeviceStatus.FeedbackStatusNotPresent) {
            FaultCode.write(FaultCode.Codes.RIGHT_ENCODER_NOT_FOUND);
        } else {
            rightMotor1.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
            rightMotor1.setEncPosition(0);
            rightMotor1.configEncoderCodesPerRev(360);
            DataLogger.addDataElement("right encoder pos", () -> rightMotor1.getPosition());
            DataLogger.addDataElement("right encoder vel", () -> rightMotor1.getSpeed());
            DataLogger.addDataElement("right closed loop error", () -> rightMotor1.getError());
            DataLogger.addDataElement("right output voltage", () -> rightMotor1.getOutputVoltage());
            DataLogger.addDataElement("right set point", () -> rightMotor1.getSetpoint());
        }

        
        this.eachSlaveMotor((CANTalon t) -> {
            t.changeControlMode(CANTalon.TalonControlMode.Follower);
            t.reverseOutput(true);
        });
        leftMotor2.set(leftMotor1.getDeviceID());
        leftMotor3.set(leftMotor1.getDeviceID());
        
        rightMotor2.set(rightMotor1.getDeviceID());
        rightMotor3.set(rightMotor1.getDeviceID());

        openLoopMode = new OpenLoopMode();
        velocityMode = new VelocityMode();
        brakeMode = new BrakeMode();
        headingMode = new HeadingMode();
        motionProfileMode = new MotionProfileMode();
        adaptivePursuitMode = new AdaptivePursuitMode();
        encoderMode = new EncoderMode();
        
        // Data Logging, using the follower will have it always
        // return applied throttle, regardless of mode
        DataLogger.addDataElement("Left Drive Power", () -> leftMotor1.get());
        DataLogger.addDataElement("Right Drive Power", () -> rightMotor1.get());
        
        currentMode = this.velocityMode;
        mode = Modes.VELOCITY;
    }
    
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        setDefaultCommand(new TeleopVelocityDrive());
    }
    
    public void eachMotor(Consumer<CANTalon> func) {
        func.accept(leftMotor1);
        func.accept(leftMotor2);
        func.accept(rightMotor1);
        func.accept(rightMotor2);
    }
    
    public void eachPrimaryMotor(Consumer<CANTalon> func) {
        func.accept(leftMotor1);
        func.accept(rightMotor1);
    }
    
    public void eachSlaveMotor(Consumer<CANTalon> func) {
        func.accept(leftMotor2);
        func.accept(rightMotor2);
        func.accept(leftMotor3);
        func.accept(rightMotor3);
    }
    
    public void setMode(Modes m) {
        Logger.debug("drive train set mode " + m + " from " + mode + " we are running: " + running);
        if (mode == m) return;
        
        synchronized (modeRunningLock) {
        boolean was_running = running;
        if (running) {
            running = false;
            currentMode.onStop();
        }
        
        switch (m) {
        case OPEN_LOOP:
            currentMode = openLoopMode;
            break;
        
        case VELOCITY:
            currentMode = velocityMode;
            break;
            
        case BRAKE:
            currentMode = brakeMode;
            break;
            
        case HEADING:
            currentMode = headingMode;
            break;
            
        case ADAPTIVE_PURSUIT:
            currentMode = adaptivePursuitMode;
            break;
            
        case MOTION_PROFILE:
            currentMode = motionProfileMode;
            break;
                        
        case ENCODER:
            currentMode = encoderMode;
            break;
                        
        default:
            // TODO set fault
        }
        
        mode = m;
        if (was_running)
            Logger.debug("call on start");
            onStart();
        }
    }    

    public void onStart() {
        synchronized (modeRunningLock) {
            Logger.debug("in onStart: " + running);
            currentMode.onStart();
            Logger.debug("should be started");
            // Logger.flush();
            running = true;
        }
    }

    public void onLoop() {
        synchronized (modeRunningLock) {
            if (running) {
              currentMode.onLoop();
            }
        }
    }

    public void onStop() {
        synchronized (modeRunningLock) {
            running = false;
            currentMode.onStop();
        }
    }
    
    public double getLeftVelocity() {
        return leftMotor1.getSpeed();
    }
    
    public double getRightVelocity() {
        return rightMotor1.getSpeed();
    }
    
    public double getVelocity() {
        return (getLeftVelocity() + getRightVelocity()) / 2;
    }
    
    public double getLeftDistance() {
        return leftMotor1.getPosition();
    }
    
    public int getLeftEncoder() {
    	return leftMotor1.getEncPosition();
    }
    
    public int getRightEncoder() {
    	return rightMotor1.getEncPosition();
    }
    
    public double getRightDistance() {
        return rightMotor1.getPosition();
    }
    
    public void resetDistance() {
        this.eachPrimaryMotor((CANTalon t) -> {
           t.setPosition(0);
           t.setEncPosition(0); 
        });
    }

    public double getAbsVelocity() {
        return Math.abs(getAverageVelocity());
    }

    public double getAverageVelocity() {
        double leftVel = leftMotor1.getSpeed();
        double rightVel = rightMotor1.getSpeed();
        double v = (leftVel  + rightVel) / 2;
        return v;
    }
    
    public double getRequestedPower() {
        return Math.abs(leftRequestedPower + rightRequestedPower) / 2;
    }
    
    public void set(double left, double right) {
        leftMotor1.set(left);
        rightMotor1.set(right);
    }

    public void stop() {
        set(0,0);
    }
    
    public void teleop(Joystick driverLeft, Joystick driverRight) {
        teleop(driverLeft, driverRight, null);
    }

    public void teleop(Joystick driverLeft, Joystick driverRight, Joystick coPilot) {
        teleop(driverLeft, driverRight, coPilot, 0);
    }
    
    public void teleop(Joystick driverLeft, Joystick driverRight, Joystick coPilot, double straighten) {
        // NOTE this is where you need to make changes if we switch to a
        // single controller, etc. 
        double leftPower = filter.filter(driverLeft.getRawAxis(1));
        double rightPower = filter.filter(driverRight.getRawAxis(1));
        
        leftRequestedPower = leftPower - straighten;
        rightRequestedPower = rightPower + straighten;
//        if(straighten != 0)
//                rightRequestedPower = -rightRequestedPower;
        // sub-modes should map -1 to 1 into their desired ranges
        currentMode.teleop(leftRequestedPower, rightRequestedPower);

        // coPilot currently unused.
    }

    public void teleopArcade(double moveValue, double rotateValue) {
        // NOTE this is where you need to make changes if we switch to a
        // single controller, etc.
        moveValue = filter.filter(limit(moveValue));
        rotateValue = filter.filter(limit(rotateValue));

        double leftMotorSpeed;
        double rightMotorSpeed;

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        currentMode.teleop(leftMotorSpeed, rightMotorSpeed);
    }

    public void start() {
        synchronized (modeRunningLock) {
            Logger.debug("drivetrain start");
            currentMode.onStart();
        }
    }

    /*
    public boolean getDrivingStraight() {
        return drivingStraight;
    }
    public void setDrivingStraight(boolean val) {
        //if (drivingStraight != val) {
            if (drivingStraight == val) {
                this.driveTheta = RobotMap.navx.getFusedHeading();
            }
        //}
    }
    */
}

