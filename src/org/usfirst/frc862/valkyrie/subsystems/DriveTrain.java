package org.usfirst.frc862.valkyrie.subsystems;

import static org.usfirst.frc862.util.LightningMath.limit;

import java.util.function.Consumer;

import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.util.FaultCode;
import org.usfirst.frc862.util.FaultCode.Codes;
import org.usfirst.frc862.util.JoystickFilter;
import org.usfirst.frc862.util.LightningMath;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Loop;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.commands.TeleopVelocityDrive;
import org.usfirst.frc862.valkyrie.subsystems.modes.BrakeMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.EncoderMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.HeadingMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.MotionMagicMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.MotionProfileMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.OpenLoopMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.SubsystemMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.TestMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.VelocityMode;

import com.ctre.CANTalon;
import com.ctre.CANTalon.MotionProfileStatus;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
@SuppressWarnings("unused")
public class DriveTrain extends Subsystem implements Loop {

    public enum Modes {
        OPEN_LOOP, VELOCITY, BRAKE, HEADING, MOTION_PROFILE, 
        ENCODER, MOTION_MAGIC, TEST
    }    

    private final Object modeRunningLock = new Object();
    private Modes mode;
    OpenLoopMode openLoopMode;
    VelocityMode velocityMode;
    BrakeMode brakeMode;
    HeadingMode headingMode;
    MotionMagicMode motionMagicMode;
    public MotionProfileMode motionProfileMode;
    SubsystemMode currentMode;

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
    private JoystickFilter filter = new JoystickFilter(Constants.deadband, Constants.MinCommandedPower, 1, JoystickFilter.Mode.LINEAR);
    private MotionProfileStatus status = new MotionProfileStatus();
    private double leftRequestedPower;
    private double rightRequestedPower;
    private EncoderMode encoderMode;
    private TestMode testMode;
    private double straightAdjust = 0;
    private double slowUntil = 0;

    // private double start;
    // private double stop;

    public DriveTrain() {
        initialize();
    }

    public void configure_test_mode() {
        eachPrimaryMotor((CANTalon t) -> {
            t.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
            t.set(0);
        });
        eachSlaveMotor((CANTalon t) -> {
            t.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
            t.set(0);
        });
    }

    public void configure_follow_modes() {
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
        }


        this.eachSlaveMotor((CANTalon t) -> {
            t.changeControlMode(CANTalon.TalonControlMode.Follower);
            t.reverseOutput(true);
        });
        leftMotor2.set(leftMotor1.getDeviceID());
        leftMotor3.set(leftMotor1.getDeviceID());

        rightMotor2.set(rightMotor1.getDeviceID());
        rightMotor3.set(rightMotor1.getDeviceID());

    }

    private void initialize() {
        configure_follow_modes();

        DataLogger.addDataElement("left encoder pos", () -> leftMotor1.getPosition());
        DataLogger.addDataElement("left encoder vel", () -> leftMotor1.getSpeed());
        DataLogger.addDataElement("left closed loop error", () -> leftMotor1.getError());
        DataLogger.addDataElement("left output voltage", () -> leftMotor1.getOutputVoltage());
        DataLogger.addDataElement("left set point", () -> leftMotor1.getSetpoint());

        DataLogger.addDataElement("right encoder pos", () -> rightMotor1.getPosition());
        DataLogger.addDataElement("right encoder vel", () -> rightMotor1.getSpeed());
        DataLogger.addDataElement("right closed loop error", () -> rightMotor1.getError());
        DataLogger.addDataElement("right output voltage", () -> rightMotor1.getOutputVoltage());
        DataLogger.addDataElement("right set point", () -> rightMotor1.getSetpoint());

        // Data Logging, using the follower will have it always
        // return applied throttle, regardless of mode
        DataLogger.addDataElement("Left Drive Power", () -> leftMotor1.get());
        DataLogger.addDataElement("Right Drive Power", () -> rightMotor1.get());

        DataLogger.addDataElement("pegdetected", () -> Robot.gearDetector.pegPresent() ? 100.0 : 0.0);

        openLoopMode = new OpenLoopMode();
        velocityMode = new VelocityMode();
        brakeMode = new BrakeMode();
        headingMode = new HeadingMode();
        motionMagicMode = new MotionMagicMode();
        motionProfileMode = new MotionProfileMode();
        encoderMode = new EncoderMode();
        testMode = new TestMode();

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

    public void leftPrimaryMotor(Consumer<CANTalon> func) {
        func.accept(leftMotor1);
    }

    public void rightPrimaryMotor(Consumer<CANTalon> func) {
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

            case MOTION_PROFILE:
                currentMode = motionProfileMode;
                break;
                
            case MOTION_MAGIC:
                currentMode = motionMagicMode;
                break;

            case TEST:
                currentMode = testMode;
                break;

            case ENCODER:
                currentMode = encoderMode;
                break;

            default:
                Logger.error("Invalid Mode: " + m.toString());
                FaultCode.write(Codes.INTERNAL_ERROR);
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
        return LightningMath.rpm2fps(leftMotor1.getSpeed());
    }

    public double getRightVelocity() {
        return LightningMath.rpm2fps(rightMotor1.getSpeed());
    }

    public double getVelocity() {
        return (getLeftVelocity() + getRightVelocity()) / 2;
    }

    public double getLeftDistance() {
        return LightningMath.rotations2feet(leftMotor1.getPosition());
    }

    public int getLeftEncoder() {
        return leftMotor1.getEncPosition();
    }

    public int getRightEncoder() {
        return rightMotor1.getEncPosition();
    }

    public double getRightDistance() {
        return LightningMath.rotations2feet(rightMotor1.getPosition());
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

    public void slowForSeconds(double seconds) {
        slowUntil = Timer.getFPGATimestamp() + seconds;
    }

    public void set(double left, double right) {
        if (Timer.getFPGATimestamp() < slowUntil) {
            leftMotor1.set(this.getAverageVelocity());
            rightMotor1.set(getAverageVelocity());
        } else {
            leftMotor1.set(left - straightAdjust);
            rightMotor1.set(right + straightAdjust);
        }
    }

    public void stop() {
        set(0,0);
    }

    public void teleop(Joystick driverLeft, Joystick driverRight) {
        teleop(driverLeft, driverRight, 0);
    }

    public void teleop(Joystick driverLeft, Joystick driverRight, double straighten) {
        // NOTE this is where you need to make changes if we switch to a
        // single controller, etc.
        teleop(driverLeft.getRawAxis(1), driverRight.getRawAxis(1));
    }

    public void teleop(double left, double right) {
        teleop(left, right, 0);
    }

    public void teleop(double left, double right, double straighten) {
        double leftPower = filter.filter(left);
        double rightPower = filter.filter(right);

        leftRequestedPower = leftPower - straighten;
        rightRequestedPower = rightPower + straighten;
        //        if(straighten != 0)
        //                rightRequestedPower = -rightRequestedPower;
        // sub-modes should map -1 to 1 into their desired ranges
        currentMode.teleop(leftRequestedPower, rightRequestedPower);
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

    public double getGyroAngle() {
        return navx.getFusedHeading();
    }

    public double getGyroAngleInRadians() {
        return getGyroAngle() * Math.PI / 180;
    }

    public void configureLowGear() {
        Robot.driveTrain.leftPrimaryMotor((CANTalon t) ->{
            t.setPID(Constants.velocityPTermLow, 0, 0);
            t.setF(Constants.velocityFeedForwardLLow);
        });

        Robot.driveTrain.rightPrimaryMotor((CANTalon t) ->{
            t.setPID(Constants.velocityPTermLow, 0, 0);
            t.setF(Constants.velocityFeedForwardRLow);
        });
    }

    public void configureHighGear() {
        Robot.driveTrain.leftPrimaryMotor((CANTalon t) ->{
            t.setPID(Constants.velocityPTermHigh, 0, 0);
            t.setF(Constants.velocityFeedForwardLHigh);
        });

        Robot.driveTrain.rightPrimaryMotor((CANTalon t) ->{
            t.setPID(Constants.velocityPTermHigh, 0, 0);
            t.setF(Constants.velocityFeedForwardRHigh);
        });
    }

    public void setStraightAdjust(double adjustment) {
        straightAdjust  = adjustment;
    }

    public double getStraightAdjust() {
        return straightAdjust;
    }    
}

