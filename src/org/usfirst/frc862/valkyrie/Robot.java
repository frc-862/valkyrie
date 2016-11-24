package org.usfirst.frc862.valkyrie;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTableKeyNotDefined;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

import org.usfirst.frc862.util.Logger;
// import org.usfirst.frc862.valkyrie.commands.*;
import org.usfirst.frc862.valkyrie.subsystems.*;

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

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        Logger.debug("robotInit");
        RobotMap.init();
        
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrain = new DriveTrain();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // instantiate the command used for the autonomous period
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        RobotMap.navx.zeroYaw();
        
        Logger.debug("start drive train");
        driveTrain.start();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit() {
        Logger.debug("disabled robot");
        Logger.flush();
        
        driveTrain.setMode(DriveTrain.Modes.BRAKE);
        SmartDashboard.putNumber("FOO", 7);
        SmartDashboard.getNumber("PTH", 999);
    }

    public void disabledPeriodic() {
        if (Utility.getUserButton()) {
//            Logger.debug("writing configuration file");
//            (new Constants()).writeToFile();
            RobotMap.navx.zeroYaw();
        }
        
        SmartDashboard.putNumber("Heading", RobotMap.navx.getYaw());
        double x = 42;
        try {
           x = SmartDashboard.getNumber("FOO", 123);
        } catch (TableKeyNotDefinedException err) {
            // do nothing
            x = 240;
        }
        SmartDashboard.putNumber("Bar", x);
        
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        Logger.debug("Starting autonomous");
        if (autonomousCommand != null) autonomousCommand.start();
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        Logger.debug("Starting teleop");
        if (autonomousCommand != null) autonomousCommand.cancel();
        
        // TODO fix other modes and make them work
        driveTrain.setMode(DriveTrain.Modes.OPEN_LOOP);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        if (Utility.getUserButton()) {
            Logger.debug("Pressed!");
        }
        Scheduler.getInstance().run();
        
        driveTrain.teleop(oi.driver, oi.coPilot);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
