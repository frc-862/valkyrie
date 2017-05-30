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

import org.usfirst.frc862.valkyrie.commands.AlignToAirShip;
import org.usfirst.frc862.valkyrie.commands.AlignToPeg;
import org.usfirst.frc862.valkyrie.commands.ArcadeDrive;
import org.usfirst.frc862.valkyrie.commands.AutonBlueBoiler;
import org.usfirst.frc862.valkyrie.commands.AutonBlueFeeder;
import org.usfirst.frc862.valkyrie.commands.AutonRedBoiler;
import org.usfirst.frc862.valkyrie.commands.AutonRedFeeder;
import org.usfirst.frc862.valkyrie.commands.AutonStraight;
import org.usfirst.frc862.valkyrie.commands.Autoshifter;
import org.usfirst.frc862.valkyrie.commands.CalibratedClimb;
import org.usfirst.frc862.valkyrie.commands.Climb;
import org.usfirst.frc862.valkyrie.commands.ClimbOverride;
import org.usfirst.frc862.valkyrie.commands.DriveDownFieldVeerLeft;
import org.usfirst.frc862.valkyrie.commands.DriveDownFieldVeerRight;
import org.usfirst.frc862.valkyrie.commands.DriveStaightDistance;
import org.usfirst.frc862.valkyrie.commands.ExtendGearCollector;
import org.usfirst.frc862.valkyrie.commands.FullBlueBoilerAuton;
import org.usfirst.frc862.valkyrie.commands.FullRedBoilerAuton;
import org.usfirst.frc862.valkyrie.commands.ManualShifter;
import org.usfirst.frc862.valkyrie.commands.OpenLoopDriveCommand;
import org.usfirst.frc862.valkyrie.commands.ReleaseGear;
import org.usfirst.frc862.valkyrie.commands.ResetADB;
import org.usfirst.frc862.valkyrie.commands.ResetNavX;
import org.usfirst.frc862.valkyrie.commands.ResetVisionServer;
import org.usfirst.frc862.valkyrie.commands.RetractGearCollector;
import org.usfirst.frc862.valkyrie.commands.ReverseDrive;
import org.usfirst.frc862.valkyrie.commands.SystemTest;
import org.usfirst.frc862.valkyrie.commands.TestAlignPeg;
import org.usfirst.frc862.valkyrie.commands.ToggleGearCollector;
import org.usfirst.frc862.valkyrie.commands.ToggleGearEject;
import org.usfirst.frc862.valkyrie.commands.TurnOnLEDRing;
import org.usfirst.frc862.valkyrie.commands.VisionAuton;
import org.usfirst.frc862.valkyrie.commands.VisionAutonRedBoiler;
import org.usfirst.frc862.valkyrie.commands.VisionFeedStation;
import org.usfirst.frc862.valkyrie.commands.VisionGear;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
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
    public JoystickButton winchButton;
    public JoystickButton extendGearButton;
    public JoystickButton retractGearButton;
    public JoystickButton autoAlign;
    public JoystickButton retractCollectorButton;
    public Joystick driverLeft;
    public JoystickButton winchButton2;
    public JoystickButton gearEjectButton;
    public JoystickButton visionDeliver;
    public Joystick driverRight;
    public JoystickButton shiftSelectAuto;
    public JoystickButton shiftSelectManual;
    public JoystickButton openLoopDriveSwitch;
    public JoystickButton normalClimb;
    public JoystickButton climbButtonOverride;
    public JoystickButton testAlignPeg;
    public JoystickButton autonRedButton;
    public Joystick buttonThing;
    public JoystickButton coPilotCollectorOut;
    public JoystickButton coPilotCollectIn;
    public JoystickButton coPilotClimb;
    public JoystickButton coPilotClimbOverride;
    public Joystick coPilot;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        coPilot = new Joystick(3);
        
        coPilotClimbOverride = new JoystickButton(coPilot, 2);
        coPilotClimbOverride.whileHeld(new ClimbOverride());
        coPilotClimb = new JoystickButton(coPilot, 1);
        coPilotClimb.whileHeld(new CalibratedClimb());
        coPilotCollectIn = new JoystickButton(coPilot, 6);
        coPilotCollectIn.whenPressed(new RetractGearCollector());
        coPilotCollectorOut = new JoystickButton(coPilot, 5);
        coPilotCollectorOut.whenPressed(new ExtendGearCollector());
        buttonThing = new Joystick(2);
        
        autonRedButton = new JoystickButton(buttonThing, 3);
        autonRedButton.whenPressed(new AutonRedBoiler());
        testAlignPeg = new JoystickButton(buttonThing, 1);
        testAlignPeg.whenPressed(new TestAlignPeg());
        climbButtonOverride = new JoystickButton(buttonThing, 4);
        climbButtonOverride.whileHeld(new ClimbOverride());
        normalClimb = new JoystickButton(buttonThing, 2);
        normalClimb.whileHeld(new CalibratedClimb());
        openLoopDriveSwitch = new JoystickButton(buttonThing, 8);
        openLoopDriveSwitch.whileHeld(new OpenLoopDriveCommand());
        shiftSelectManual = new JoystickButton(buttonThing, 6);
        shiftSelectManual.whenPressed(new ManualShifter());
        shiftSelectAuto = new JoystickButton(buttonThing, 6);
        shiftSelectAuto.whenReleased(new Autoshifter());
        driverRight = new Joystick(1);
        
        visionDeliver = new JoystickButton(driverRight, 2);
        visionDeliver.whileHeld(new TestAlignPeg());
        gearEjectButton = new JoystickButton(driverRight, 4);
        gearEjectButton.whenPressed(new ToggleGearEject());
        winchButton2 = new JoystickButton(driverRight, 7);
        winchButton2.whileHeld(new ClimbOverride());
        driverLeft = new Joystick(0);
        
        retractCollectorButton = new JoystickButton(driverLeft, 3);
        retractCollectorButton.whenPressed(new ToggleGearCollector());
        autoAlign = new JoystickButton(driverLeft, 4);
        autoAlign.whileHeld(new AlignToPeg());
        retractGearButton = new JoystickButton(driverLeft, 10);
        retractGearButton.whileHeld(new RetractGearCollector());
        extendGearButton = new JoystickButton(driverLeft, 9);
        extendGearButton.whileHeld(new ExtendGearCollector());
        winchButton = new JoystickButton(driverLeft, 2);
        winchButton.whileHeld(new CalibratedClimb());


        // SmartDashboard Buttons
        SmartDashboard.putData("AutonBlueBoiler", new AutonBlueBoiler());
        SmartDashboard.putData("AutonBlueFeeder", new AutonBlueFeeder());
        SmartDashboard.putData("AutonRedBoiler", new AutonRedBoiler());
        SmartDashboard.putData("AutonRedFeeder", new AutonRedFeeder());
        SmartDashboard.putData("AutonStraight", new AutonStraight());
        SmartDashboard.putData("Climb", new Climb());
        SmartDashboard.putData("Arcade Drive", new ArcadeDrive());
        SmartDashboard.putData("VisionFeedStation", new VisionFeedStation());
        SmartDashboard.putData("VisionGear", new VisionGear());
        SmartDashboard.putData("Climb Override", new ClimbOverride());
        SmartDashboard.putData("TurnOnLEDRing", new TurnOnLEDRing());
        SmartDashboard.putData("VisionAutonRedBoiler", new VisionAutonRedBoiler());
        SmartDashboard.putData("VisionAuton: Do Nothing", new VisionAuton(""));
        SmartDashboard.putData("DriveStaightDistance: Forward", new DriveStaightDistance(1));
        SmartDashboard.putData("Release Gear", new ReleaseGear());
        SmartDashboard.putData("ResetVisionServer", new ResetVisionServer());
        SmartDashboard.putData("ResetADB", new ResetADB());
        SmartDashboard.putData("ToggleGearEject", new ToggleGearEject());
        SmartDashboard.putData("Reset NavX", new ResetNavX());
        SmartDashboard.putData("FullRedBoilerAuton", new FullRedBoilerAuton());
        SmartDashboard.putData("Drive Down Field Veer Left", new DriveDownFieldVeerLeft());
        SmartDashboard.putData("Drive Down Field Veer Right", new DriveDownFieldVeerRight());
        SmartDashboard.putData("AlignToAirShip", new AlignToAirShip());
        SmartDashboard.putData("TestAlignPeg", new TestAlignPeg());
        SmartDashboard.putData("Full Blue Boiler Auton", new FullBlueBoilerAuton());
        SmartDashboard.putData("ReverseDrive", new ReverseDrive());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        
        SmartDashboard.putData("SystemTest", new SystemTest());

//        new LowVoltageTrigger(RobotMap.corePowerPanel, Constants.LowVoltage, Constants.LVTriggerTime).whenActive(
//                new FaultCommand(FaultCode.Codes.LOW_MAIN_VOLTAGE)
//        );

        
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getDriverLeft() {
        return driverLeft;
    }

    public Joystick getDriverRight() {
        return driverRight;
    }

    public Joystick getButtonThing() {
        return buttonThing;
    }

    public Joystick getCoPilot() {
        return coPilot;
    }


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}

