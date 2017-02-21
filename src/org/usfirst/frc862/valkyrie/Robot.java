package org.usfirst.frc862.valkyrie;

import org.usfirst.frc862.util.CrashTracker;
import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Looper;
// import org.usfirst.frc862.valkyrie.commands.*;
import org.usfirst.frc862.valkyrie.subsystems.Core;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;
import org.usfirst.frc862.valkyrie.subsystems.GearCollector;
import org.usfirst.frc862.valkyrie.subsystems.Shifter;
import org.usfirst.frc862.valkyrie.subsystems.Winch;
import org.usfirst.frc862.valkyrie.subsystems.Core.Ultrasonic;

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

    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static DriveTrain driveTrain;
    public static Core core;
    public static Winch winch;
    public static Shifter shifter;
    public static GearCollector gearCollector;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    
    public SendableChooser<Command> autonChooser;
    public Looper driveTrainLooper;
    public Looper backgroundLooper;
    
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
            
            driveTrainLooper = new Looper(Constants.driveTrainLoopRate);
            backgroundLooper = new Looper(Constants.backgroundLoopRate);

            backgroundLooper.register(Logger.getWriter());
            backgroundLooper.register(DataLogger.getLogger().getLogWriter());
            driveTrainLooper.register(DataLogger.getLogger());
            driveTrainLooper.register(driveTrain);

            backgroundLooper.start();
            driveTrainLooper.start();

            // TODO add auton modes
            autonChooser = new SendableChooser<Command>();
            autonChooser.addDefault("Do Nothing", null);
            // autonChooser.addDefault("Center Lift", new StraightDynamicProfile());
            
            SmartDashboard.putData("Auton Mode", autonChooser);
            SmartDashboard.putNumber("Climb Power", 1.0);
            SmartDashboard.putNumber("Dynamic Motion Profile X", 1.0);
            SmartDashboard.putNumber("Dynamic Motion Profile Y", 1.0);
            SmartDashboard.putNumber("Dynamic Motion Profile Angle", 90.0);
            
            SmartDashboard.putBoolean("Use Crash Downshift", true);
            SmartDashboard.putBoolean("Use Coast Downshift", true);
            SmartDashboard.putBoolean("Use Velocity Upshift", true);
            
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    public void resetDataLog() {
        driveTrainLooper.stop();
        DataLogger.new_file();
        driveTrainLooper.start();
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
            SmartDashboard.putNumber("Heading", RobotMap.navx.getYaw());

            SmartDashboard.putNumber("Joy1", oi.driverLeft.getRawAxis(1));
            SmartDashboard.putNumber("Joy2", oi.driverRight.getRawAxis(1));
            SmartDashboard.putNumber("Joy2LR", oi.driverRight.getRawAxis(0));
            // Scheduler.getInstance().run();
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
            
            // TODO fix other modes and make them work
            // driveTrain.setMode(DriveTrain.Modes.OPEN_LOOP);
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
            }
            Scheduler.getInstance().run();
            SmartDashboard.putNumber("Left Pos", RobotMap.driveTrainLeftMotor1.getPosition());
            SmartDashboard.putNumber("Right Pos", RobotMap.driveTrainRightMotor1.getPosition());
            SmartDashboard.putNumber("Left Vel", RobotMap.driveTrainLeftMotor1.getSpeed());
            SmartDashboard.putNumber("Right Vel", RobotMap.driveTrainRightMotor1.getSpeed());
            SmartDashboard.putNumber("Total Power", RobotMap.corePowerPanel.getTotalCurrent());
            SmartDashboard.putNumber("Voltage", RobotMap.corePowerPanel.getVoltage());
            
            
            SmartDashboard.putNumber("Average Velocity", Robot.driveTrain.getAverageVelocity());
            SmartDashboard.putNumber("Requested Power", Robot.driveTrain.getRequestedPower());

            SmartDashboard.putNumber("Ultra1", Robot.core.getUltrasonic(Ultrasonic.Front));
            SmartDashboard.putNumber("Ultra2", Robot.core.getUltrasonic(Ultrasonic.Left));
            SmartDashboard.putNumber("Ultra3", Robot.core.getUltrasonic(Ultrasonic.Right));
            SmartDashboard.putNumber("Ultra1", Robot.core.getUltrasonic(Ultrasonic.Front));
            
            //SmartDashboard.putNumber("Beam", Robot.core);
//            
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

//    @Override
//    public void testInit() {
//        try {
//            CrashTracker.logTestInit();
//
//            // TODO Auto-generated method stub
//            super.testInit();
//        } catch (Throwable t) {
//            CrashTracker.logThrowableCrash(t);
//            throw t;
//        }
//    }

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
