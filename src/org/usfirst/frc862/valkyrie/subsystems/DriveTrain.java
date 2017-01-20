package org.usfirst.frc862.valkyrie.subsystems;

import java.util.function.Consumer;

import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.util.FaultCode;
import org.usfirst.frc862.util.JoystickFilter;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Loop;
import org.usfirst.frc862.util.LoopingSubsystem;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.RobotMap;
import org.usfirst.frc862.valkyrie.commands.*;
import org.usfirst.frc862.valkyrie.subsystems.modes.BrakeMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.HeadingMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.MotionProfileMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.OpenLoopMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.SubsystemMode;
import org.usfirst.frc862.valkyrie.subsystems.modes.VelocityMode;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.CANTalon;
import com.ctre.CANTalon.MotionProfileStatus;
import com.kauailabs.navx.frc.AHRS;

/**
 *
 */
@SuppressWarnings("unused")
public class DriveTrain extends Subsystem implements Loop {

    public enum Modes {
        OPEN_LOOP, VELOCITY, BRAKE, HEADING, MOTION_PROFILE
    }    
    
    private final Object modeRunningLock = new Object();
    private Modes mode;
    OpenLoopMode openLoopMode;
    VelocityMode velocityMode;
    BrakeMode brakeMode;
    HeadingMode headingMode;
    MotionProfileMode motionProfileMode;
    SubsystemMode currentMode;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon leftMotor1 = RobotMap.driveTrainLeftMotor1;
    private final CANTalon leftMotor2 = RobotMap.driveTrainLeftMotor2;
    private final CANTalon rightMotor1 = RobotMap.driveTrainRightMotor1;
    private final CANTalon rightMotor2 = RobotMap.driveTrainRightMotor2;
    private final DoubleSolenoid shifter = RobotMap.driveTrainShifter;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final AHRS navx = RobotMap.navx;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    private boolean running = false;
    private JoystickFilter filter = new JoystickFilter(Constants.deadband, 0, 1, JoystickFilter.Mode.SQUARED);
    private MotionProfileStatus status = new MotionProfileStatus();

    // private double start;
    // private double stop;
    
    public DriveTrain() {
        initialize();
    }
    
    private void initialize() {
        this.eachSlaveMotor((CANTalon t) -> {
            t.changeControlMode(CANTalon.TalonControlMode.Follower);
        });
        leftMotor2.set(leftMotor1.getDeviceID());
        rightMotor2.set(rightMotor1.getDeviceID());

        leftMotor1.reverseSensor(false);
        leftMotor1.reverseOutput(false);
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
        
        openLoopMode = new OpenLoopMode();
        velocityMode = new VelocityMode();
        brakeMode = new BrakeMode();
        headingMode = new HeadingMode();
        motionProfileMode = new MotionProfileMode();
        
        // Data Logging, using the follower will have it always
        // return applied throttle, regardless of mode
        DataLogger.addDataElement("Left Drive Power", () -> leftMotor2.get());
        DataLogger.addDataElement("Right Drive Power", () -> rightMotor2.get());
        DataLogger.addDataElement("heading", () -> navx.getFusedHeading());
        
        currentMode = brakeMode;
        mode = Modes.BRAKE;
    }
    
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
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
              Logger.debug("Looping in " + currentMode.getClass().getCanonicalName() + " at " + Timer.getFPGATimestamp() + " ??? " + running);
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
    
    public double getRightDistance() {
        return rightMotor1.getPosition();
    }
    
    public void resetDistance() {
        this.eachPrimaryMotor((CANTalon t) -> {
           t.setPosition(0);
           t.setEncPosition(0); 
        });
    }
    
    public void set(double left, double right) {
        leftMotor1.set(left);
        rightMotor1.set(right);
    }

    public void teleop(Joystick driverLeft, Joystick driverRight) {
        teleop(driverLeft, driverRight, null);
    }

    public void teleop(Joystick driverLeft, Joystick driverRight, Joystick coPilot) {
        // NOTE this is where you need to make changes if we switch to a
        // single controller, etc. 
        double leftPower = filter.filter(driverLeft.getRawAxis(1));
        double rightPower = filter.filter(driverRight.getRawAxis(1));
        
        // sub-modes should map -1 to 1 into their desired ranges
        currentMode.teleop(-leftPower, rightPower);

        // coPilot currently unused.
    }

    public void downShift() {
        currentMode.downShift();
    }
    
    public void upShift() {
        currentMode.upShift();
    }

    public void start() {
        synchronized (modeRunningLock) {
            Logger.debug("drivetrain start");
            currentMode.onStart();
        }
    }
}

