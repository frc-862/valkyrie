package org.usfirst.frc862.valkyrie.subsystems;

import java.util.function.Consumer;

import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.util.FaultCode;
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
    
    private int shiftingMode = 0;
    //low = 0 high = 1
    private boolean commandDownshift, commandUpshift = false;
    private double calibratedDS, calibratedUS;
    //Set calibrated values
    private boolean nothingWrong;
    private double velocity;
    //Set automatically
    

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

        leftMotor1.reverseSensor(true);
        leftMotor1.reverseOutput(false);
        leftMotor2.reverseOutput(false);        
        if (leftMotor1.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative) != 
                CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
            FaultCode.write(FaultCode.Codes.LEFT_ENCODER_NOT_FOUND);
        } else {
            DataLogger.addDataElement("left encoder pos", () -> leftMotor1.getEncPosition());
            DataLogger.addDataElement("left encoder vel", () -> leftMotor1.getEncVelocity());
        }
        
        rightMotor1.reverseSensor(false);
        rightMotor1.reverseOutput(true);
        rightMotor2.reverseOutput(true);        
        if (rightMotor1.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative) !=
                CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
            FaultCode.write(FaultCode.Codes.RIGHT_ENCODER_NOT_FOUND);
        } else {
            DataLogger.addDataElement("right encoder pos", () -> rightMotor1.getEncPosition());
            DataLogger.addDataElement("right encoder vel", () -> rightMotor1.getEncVelocity());
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
    
    public void shiftingLoop() {
    	//check for drivetrain problems here
    	//set nothingWrong

    	if (shiftingMode == 0) {
    		if (velocity > calibratedUS && nothingWrong) {
    			upShift();
    			//shiftingMode = 1; when upshift finishes
    		}
    	}
    	
    	if (shiftingMode == 1) {
    		if (velocity < calibratedDS && nothingWrong) {
    			downShift();
    			//shiftingMode = 0; when downshift finishes
    		}
    	}
    	
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
        if (mode == m) return;
        
        synchronized (modeRunningLock) {
        boolean was_running = running;
        if (running) 
            currentMode.onStop();
        
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
            currentMode.onStart();
        }
    }    

    public void onStart() {
        synchronized (modeRunningLock) {
            currentMode.onStart();
        }
    }

    public void onLoop() {
        synchronized (modeRunningLock) {
            currentMode.onLoop();
        }
    }

    public void onStop() {
        synchronized (modeRunningLock) {
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

    public void teleop(Joystick driver, Joystick coPilot) {
        currentMode.teleop(driver, coPilot);
    }

    public void downShift() {
        currentMode.downShift();
    }
    
    public void upShift() {
        currentMode.upShift();
    }
}

