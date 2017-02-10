// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc862.valkyrie;

import org.usfirst.frc862.util.FaultCode;
import org.usfirst.frc862.util.FaultCommand;
import org.usfirst.frc862.valkyrie.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.LightningButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc862.valkyrie.triggers.HighCurrentTrigger;
import org.usfirst.frc862.valkyrie.triggers.LosingPushingBattleTrigger;
import org.usfirst.frc862.valkyrie.triggers.LowVoltageTrigger;
import org.usfirst.frc862.valkyrie.triggers.CoastTrigger;
import org.usfirst.frc862.valkyrie.triggers.VelocityTalonUpshiftTrigger;
import org.usfirst.frc862.valkyrie.triggers.CrashTrigger;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
@SuppressWarnings("WeakerAccess")
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public JoystickButton driveStraightBtn;
    public JoystickButton cmdDownshift1;
    public JoystickButton winchButton;
    public JoystickButton upshiftButton;
    public JoystickButton downshiftButton;
    public JoystickButton larkinButton;
    public Joystick driverLeft;
    public JoystickButton cmdUpShift2;
    public JoystickButton cmdDownshift2;
    public JoystickButton winchButton2;
    public Joystick driverRight;
    public Joystick coPilot;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        coPilot = new Joystick(2);
        
        driverRight = new Joystick(1);
        
        winchButton2 = new JoystickButton(driverRight, 7);
        winchButton2.whileHeld(new CalibratedClimb());
        cmdDownshift2 = new JoystickButton(driverRight, 2);
        cmdDownshift2.whenPressed(new DownShift());
        cmdUpShift2 = new JoystickButton(driverRight, 1);
        cmdUpShift2.whenPressed(new Upshift());
        driverLeft = new Joystick(0);
        
        larkinButton = new JoystickButton(driverLeft, 12);
        larkinButton.whileHeld(new LarkinCmd());
        downshiftButton = new JoystickButton(driverLeft, 16);
        downshiftButton.whenPressed(new DownShift());
        upshiftButton = new JoystickButton(driverLeft, 11);
        upshiftButton.whenPressed(new Upshift());
        winchButton = new JoystickButton(driverLeft, 8);
        winchButton.whileHeld(new Climb());
        cmdDownshift1 = new JoystickButton(driverLeft, 2);
        cmdDownshift1.whenPressed(new DownShift());
        driveStraightBtn = new JoystickButton(driverLeft, 1);
        driveStraightBtn.whileHeld(new DriveStraight());


        // SmartDashboard Buttons
        SmartDashboard.putData("Do Nothing", new DoNothing());
        SmartDashboard.putData("Upshift", new Upshift());
        SmartDashboard.putData("DownShift", new DownShift());
        SmartDashboard.putData("WriteConstants", new WriteConstants());
        SmartDashboard.putData("Follow Motion Profile", new FollowMotionProfile());
        SmartDashboard.putData("Climb", new Climb());
        SmartDashboard.putData("CalibratedClimb", new CalibratedClimb());
        SmartDashboard.putData("PauseCompressor", new PauseCompressor());
        SmartDashboard.putData("Rotate: left", new Rotate(90));
        SmartDashboard.putData("Rotate: right", new Rotate(-90));
        SmartDashboard.putData("Rotate: reverse", new Rotate(180));
        SmartDashboard.putData("DriveStraight", new DriveStraight());
        SmartDashboard.putData("LarkinCmd", new LarkinCmd());
        SmartDashboard.putData("Get Data Packet", new GetDataPacket());
        SmartDashboard.putData("Open Loop Drive Command", new OpenLoopDriveCommand());
        SmartDashboard.putData("Arcade Drive", new ArcadeDrive());
        SmartDashboard.putData("DynamicCheese", new DynamicCheese());
        SmartDashboard.putData("ResetDataLogger", new ResetDataLogger());
        SmartDashboard.putData("DynamicExtraCheese", new DynamicExtraCheese());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        // Todo integrate either/two button commands into robotbuilder
        // new EitherButtonTrigger(cmdUpshift1, cmdUpShift2).whenActive(new Upshift());

        if (false) {
        new LowVoltageTrigger(RobotMap.corePowerPanel, Constants.LowVoltage, Constants.LVTriggerTime).whenActive(
                new FaultCommand(FaultCode.Codes.LOW_MAIN_VOLTAGE)
        );

        Trigger hctrigger = new HighCurrentTrigger(RobotMap.corePowerPanel, Constants.HighCurrentThreshold, Constants.HCTriggerTime);
        
        hctrigger.whenActive(
                // TODO disable compressor
                // TODO only shift if in high gear
                new DownShift()
        );
//        hctrigger.

        // TODO duration may be too long, test test test
        new CrashTrigger(RobotMap.navx, Constants.CrashDeacceleration, Constants.CrashDuration).whenActive(
                // TODO disable compressor
                // TODO only shift if in high gear
                new DownShift()
        );

        new LosingPushingBattleTrigger(Robot.driveTrain, Constants.LostBattleTriggerTime).whenActive(
                new DownShift()
        );

        new CoastTrigger(Robot.driveTrain, Constants.CoastVelocity,
                Constants.CoastTriggerTime, Constants.MaxCoastPower).whenActive(
                // TODO only shift if in high gear
                new DownShift()
        );

        //Luke shook Nicole
        new VelocityTalonUpshiftTrigger(Robot.driveTrain, Constants.MinRequestedPowerForUpshift,
                Constants.MinUpshiftVelocity, Constants.VelocityUpshiftTime).whenActive(
                new Upshift()
        );
        }
        /*
        new EncoderFailure(RobotMap.corePowerPanel, Constants.HighCurrentThreshold, Constants.TriggerTime).whenActive(
                new LogCommand(() -> "Current warning: " + RobotMap.corePowerPanel.getTotalCurrent())
        );
        */
//        if (VelocityTalonUpshiftTrigger.this.get()) //how does this work I don't get it
//            Robot.driveTrain.upShift();
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getDriverLeft() {
        return driverLeft;
    }

    public Joystick getDriverRight() {
        return driverRight;
    }

    public Joystick getCoPilot() {
        return coPilot;
    }


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}

