package org.usfirst.frc862.valkyrie;

import org.usfirst.frc862.trajectory.RobotState;
import org.usfirst.frc862.trajectory.RobotStateEstimator;
import org.usfirst.frc862.util.CrashTracker;
import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.util.FaultCode;
import org.usfirst.frc862.util.LightningMath;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Looper;
import org.usfirst.frc862.valkyrie.commands.AutonBlueBoiler;
import org.usfirst.frc862.valkyrie.commands.AutonBlueFeeder;
import org.usfirst.frc862.valkyrie.commands.AutonRedBoiler;
import org.usfirst.frc862.valkyrie.commands.AutonRedFeeder;
import org.usfirst.frc862.valkyrie.commands.AutonStraight;
import org.usfirst.frc862.valkyrie.commands.FullBlueBoilerAuton;
import org.usfirst.frc862.valkyrie.commands.FullBlueFeederAuton;
import org.usfirst.frc862.valkyrie.commands.FullRedBoilerAuton;
import org.usfirst.frc862.valkyrie.commands.FullRedFeederAuton;
import org.usfirst.frc862.valkyrie.commands.SystemTest;
// import org.usfirst.frc862.valkyrie.commands.*;
import org.usfirst.frc862.valkyrie.subsystems.Core;
import org.usfirst.frc862.valkyrie.subsystems.Core.Ultrasonic;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;
import org.usfirst.frc862.valkyrie.subsystems.GearCollector;
import org.usfirst.frc862.valkyrie.subsystems.GearDetector;
import org.usfirst.frc862.valkyrie.subsystems.Shifter;
import org.usfirst.frc862.valkyrie.subsystems.Winch;
import org.usfirst.frc862.vision.TargetInfo;
import org.usfirst.frc862.vision.VisionProcessor;
import org.usfirst.frc862.vision.VisionServer;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.hal.ControlWord;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command autonomousCommand;
    VisionServer visionServer;
    
    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static DriveTrain driveTrain;
    public static Core core;
    public static Winch winch;
    public static Shifter shifter;
    public static GearCollector gearCollector;
    public static GearDetector gearDetector;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    
    public SendableChooser<Command> autonChooser;
    public Looper fastLooper;
    public Looper mediumLooper;
    public Looper slowLooper;
    
    public static Robot me;
    
    public Robot() {
        CrashTracker.logRobotConstruction();
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    double lastTime = Timer.getFPGATimestamp();
    
    public void robotInit() {
        try {
            me = this;
            CrashTracker.logRobotInit();

            Logger.debug("robotInit");
            RobotMap.init();

            visionServer = VisionServer.getInstance();
            visionServer.addVisionUpdateReceiver(VisionProcessor.getInstance());

            ControlWord cw = new ControlWord();
            DataLogger.addDataElement("status", () -> {
                int status = 0;
                
                HAL.getControlWord(cw);
                status |= cw.getEnabled()     ?  1 : 0;
                status |= cw.getDSAttached()  ?  2 : 0;
                status |= cw.getFMSAttached() ?  4 : 0;
                status |= cw.getEStop()       ?  8 : 0;
                status |= cw.getAutonomous()  ? 16 : 0;
                status |= cw.getTest()        ? 32 : 0;
                
                // status |= 64 << HAL.getAllianceStation().ordinal();
                
                return status;
            });

            // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrain = new DriveTrain();
        core = new Core();
        winch = new Winch();
        shifter = new Shifter();
        gearCollector = new GearCollector();
        gearDetector = new GearDetector();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
            // OI must be constructed after subsystems. If the OI creates
            // Commands
            // (which it very likely will), subsystems are not guaranteed to be
            // constructed yet. Thus, their requires() statements may grab null
            // pointers. Bad news. Don't move it.
            oi = new OI();

            // instantiate the command used for the autonomous period
            // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
            Logger.debug("start drive train");
            
            fastLooper = new Looper(Constants.fastLoopRate);
            mediumLooper = new Looper(Constants.mediumLoopRate);
            slowLooper = new Looper(Constants.slowLoopRate);

            slowLooper.register(Logger.getWriter());
            slowLooper.register(DataLogger.getLogger().getLogWriter());
            mediumLooper.register(DataLogger.getLogger());
            mediumLooper.register(VisionProcessor.getInstance());
            fastLooper.register(RobotStateEstimator.getInstance());

            slowLooper.start();
            mediumLooper.start();
            fastLooper.start();

            autonChooser = new SendableChooser<Command>();
            autonChooser.addDefault("Do Nothing", null);
            autonChooser.addObject("Blue Boiler", new FullBlueBoilerAuton());
            autonChooser.addObject("Blue Feeder", new FullBlueFeederAuton());
            autonChooser.addObject("Red Boiler", new FullRedBoilerAuton());
            autonChooser.addObject("Red Feeder", new FullRedFeederAuton());
            autonChooser.addObject("Straight", new AutonStraight());            
            SmartDashboard.putData("Auton Mode", autonChooser);
                        
            FaultCode.update();
            SystemTest.update();
            
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    public void resetDataLog() {
        mediumLooper.stop();
        DataLogger.new_file();
        mediumLooper.start();
    }
    
    /**
     * This function is called when the disabled button is hit. You can use it
     * to reset subsystems before shutting down.
     */
    public void disabledInit() {
        try {
            CrashTracker.logDisabledInit();
            Logger.debug("disabled robot");

            driveTrain.setMode(DriveTrain.Modes.BRAKE);
            driveTrain.start();
            
            Logger.flush();
            DataLogger.flush();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    public void disabledPeriodic() {
        try {
            SmartDashboard.putNumber("Heading", Robot.driveTrain.getGyroAngle());
            Scheduler.getInstance().run();
            
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    public void autonomousInit() {
        try {
            CrashTracker.logAutoInit();

            // schedule the autonomous command (example)
            Logger.debug("Starting autonomous");
            shifter.downShift();
            autonomousCommand = (Command) autonChooser.getSelected();
            if (autonomousCommand != null)
                autonomousCommand.start();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        try {
            Scheduler.getInstance().run();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    public void teleopInit() {
        try {
            CrashTracker.logTeleopInit();

            // This makes sure that the autonomous stops running when
            // teleop starts running. If you want the autonomous to
            // continue until interrupted by another command, remove
            // this line or comment it out.
            Logger.debug("Starting teleop");
            if (autonomousCommand != null)
                autonomousCommand.cancel();

            shifter.downShift();
            
            driveTrain.setMode(DriveTrain.Modes.VELOCITY);
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        try {
            if (Utility.getUserButton()) {
                Logger.debug("Pressed!");
                VisionServer.getInstance().requestAppRestart();
            }
            Scheduler.getInstance().run();
            
            if (oi.driverLeft.getRawButton(11)) {
            	core.greenLED();
            } else if (oi.driverLeft.getRawButton(12)) {
            	core.rainbowLED();
            } else if (oi.driverLeft.getRawButton(13)) {
            	core.orangeAndBlueLED();
            }
            TargetInfo target = RobotState.getInstance().getCurrentVisionTarget();
            if (target != null) {
               SmartDashboard.putNumber("Vision Lat", target.getLatidunalDistance());
               SmartDashboard.putNumber("Vision Long", target.getLongitudinalDistance());
               SmartDashboard.putNumber("Vision Theta: ", target.getTheta());
               double fudge = 40;
               double lat = target.getLatidunalDistance();
               double lon = target.getLongitudinalDistance();
               if (Math.abs(lon) < 0.000001) {
                   lon = 0.0001;
               }
               lat -= fudge;
               double theta = Math.asin(lat / lon);
               SmartDashboard.putNumber("Vision Theta2: ", Math.toDegrees(theta));
               SmartDashboard.putNumber("Vision Theta3: ", Math.toDegrees(Math.atan2(lat, lon)));
               SmartDashboard.putString("Vision coord", target.getX() + "," + target.getY() + "," + target.getZ());
            }
            
            SmartDashboard.putNumber("Left Pos", RobotMap.driveTrainLeftMotor1.getPosition());
            SmartDashboard.putNumber("Right Pos", RobotMap.driveTrainRightMotor1.getPosition());
            SmartDashboard.putNumber("Left Vel", RobotMap.driveTrainLeftMotor1.getSpeed());
            SmartDashboard.putNumber("Right Vel", RobotMap.driveTrainRightMotor1.getSpeed());
            SmartDashboard.putNumber("Total Power", RobotMap.corePowerPanel.getTotalCurrent());
            
            
            SmartDashboard.putNumber("Heading", Robot.driveTrain.getGyroAngle());
            SmartDashboard.putBoolean("Gear", Robot.gearDetector.gearPresent());
            
            Logger.flush();

        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        try {
            LiveWindow.run();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }
}
