// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc862.valkyrie.commands;

import org.usfirst.frc862.util.CommandLogger;
import org.usfirst.frc862.util.LightningTimer;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Autoshifter extends Command {
    private enum State {
        LOW_GEAR, UP_SHIFTING, DOWN_SHIFTING, HIGH_GEAR, HYSTERESIS_DELAY, STRAIGHT_CHECK, STRAIGHT_OVERRIDE
    }
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    private State state;
    private CommandLogger logger;

    @Override
    protected void end() {
        Robot.driveTrain.setStraightAdjust(0);
    }

    @Override
    protected void interrupted() {
        end();
    }

    private LightningTimer lowGearUpshiftTimer;
    private LightningTimer highGearDownshiftTimer;
    private LightningTimer lastShiftTimer;
    private LightningTimer overtakeTimer;
    private LightningTimer currentTimer;
    private LightningTimer loggerTimer;
    
    private double originalLDistance, originalRDistance, lDistDif, rDistDif, bigDif;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public Autoshifter() {

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.shifter);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        lowGearUpshiftTimer = new LightningTimer();
        highGearDownshiftTimer = new LightningTimer();
        lastShiftTimer = new LightningTimer();
        overtakeTimer = new LightningTimer();
        currentTimer = new LightningTimer();
        loggerTimer = new LightningTimer();
        
        logger = new CommandLogger(getName());
        logger.addDataElement("left_velocity");
        logger.addDataElement("right_velocity");
        logger.addDataElement("left_encoder");
        logger.addDataElement("right_encoder");
        logger.addDataElement("straighten");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        state = (Robot.shifter.isHighGear()) ? State.HIGH_GEAR : State.LOW_GEAR;
        resetTimers();
        originalLDistance = Robot.driveTrain.getLeftDistance();
        originalRDistance = Robot.driveTrain.getRightDistance();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        switch (state) {
        case HIGH_GEAR:
            high_gear();
            break;

        case HYSTERESIS_DELAY:
            logger.set("left_velocity", Robot.driveTrain.getLeftVelocity());
            logger.set("right_velocity", Robot.driveTrain.getRightVelocity());
            logger.set("left_encoder", Robot.driveTrain.getLeftDistance() - originalLDistance);
            logger.set("right_encoder", Robot.driveTrain.getRightDistance() - originalRDistance);
            logger.set("straighten", Robot.driveTrain.getStraightAdjust());
            logger.write();
            if (lastShiftTimer.hasPeriodPassed(Constants.shiftHysteresis)) {
                state = (Robot.shifter.isHighGear()) ? State.HIGH_GEAR : State.LOW_GEAR;
                logger.drain();
                logger.flush();
                resetTimers();
            }
            break;

        case LOW_GEAR:
            low_gear();
            break;

        case UP_SHIFTING:
            Robot.driveTrain.slowForSeconds(0.10);
            originalRDistance = Robot.driveTrain.getRightDistance();
            originalLDistance = Robot.driveTrain.getLeftDistance();
            Robot.shifter.upShift();
            resetTimers();
            overtakeTimer.reset();
            state = State.STRAIGHT_OVERRIDE;
            if (Robot.inTeleop) {
                Robot.core.turnOffLED();
            }
            break;

        case DOWN_SHIFTING:
            Robot.shifter.downShift();
            state = State.HYSTERESIS_DELAY;
            resetTimers();
            Robot.core.turnOnLED();
            break;

        case STRAIGHT_CHECK:
            if (overtakeTimer.hasPeriodPassed(0.01)) {
                state = State.HYSTERESIS_DELAY;
            }
            break;

        case STRAIGHT_OVERRIDE:
            logger.set("left_velocity", Robot.driveTrain.getLeftVelocity());
            logger.set("right_velocity", Robot.driveTrain.getRightVelocity());
            logger.set("left_encoder", Robot.driveTrain.getLeftDistance() - originalLDistance);
            logger.set("right_encoder", Robot.driveTrain.getRightDistance() - originalRDistance);
            logger.set("straighten", Robot.driveTrain.getStraightAdjust());
            logger.write();

            rDistDif = originalRDistance - Robot.driveTrain.getRightDistance();
            lDistDif = originalLDistance - Robot.driveTrain.getLeftDistance();
            bigDif = rDistDif - lDistDif;
            Robot.driveTrain.setStraightAdjust(Robot.driveTrain.getLeftVelocity() * .05);
            // Robot.driveTrain.setStraightAdjust(bigDif * Constants.autoshightStraightenP);
            if (overtakeTimer.hasPeriodPassed(Constants.autoshightStraightenPeriod)) {
                state = State.HYSTERESIS_DELAY;
                Robot.driveTrain.setStraightAdjust(0);
            }
            break;
        }
        
        if (loggerTimer.hasPeriodPassed(5)) {
            logger.drain();
            logger.flush();
            loggerTimer.reset();
        }
    }

    private void resetTimers() {
        lowGearUpshiftTimer.reset();
        highGearDownshiftTimer.reset();
        lastShiftTimer.reset();
        currentTimer.reset();
        //loggerTimer.reset();
    }

    private void low_gear() {
        if (Robot.driveTrain.getAbsVelocity() > Constants.MinUpshiftVelocity && Robot.driveTrain.getRequestedPower() > Constants.MinRequestedPowerForUpshift) {
//            if (lowGearUpshiftTimer.hasPeriodPassed(Constants.VelocityUpshiftTime)) {
                state = State.UP_SHIFTING;
//            }
//        } else {
//            lowGearUpshiftTimer.reset();
        }
    }

    private void high_gear() {
        if (Robot.core.getPDP().getTotalCurrent() > Constants.HighCurrentThreshold) {
            if (currentTimer.hasPeriodPassed(Constants.highCurrentTimeout)) {
                Logger.info("Stall current downshift: " + Robot.core.getPDP().getTotalCurrent());
                state = State.DOWN_SHIFTING;
                return;
            }
        } else {
            currentTimer.reset();
        }

        double crash = RobotMap.navx.getWorldLinearAccelY();
        if (crash < Constants.CrashDeacceleration) {
            Logger.info("Crash Downshift: " + crash);
            state = State.DOWN_SHIFTING;
            return;
        }

        if (Robot.driveTrain.getAbsVelocity() < Constants.CoastVelocity) {
            if (highGearDownshiftTimer.hasPeriodPassed(Constants.CoastTriggerTime)) {
                state = State.DOWN_SHIFTING;
                return;
            }
        } else {
            highGearDownshiftTimer.reset();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

}
