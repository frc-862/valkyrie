// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc862.valkyrie.subsystems;

import java.nio.ByteBuffer;

import org.usfirst.frc862.util.DataLogger;
import org.usfirst.frc862.util.Loop;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.RobotMap;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Core extends Subsystem implements Loop {
    public enum Ultrasonic {
        Front, Left, Right, Back
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final AnalogInput frontUltrasonic = RobotMap.coreFrontUltrasonic;
    private final AnalogInput leftUltrasonic = RobotMap.coreLeftUltrasonic;
    private final AnalogInput rightUltrasonic = RobotMap.coreRightUltrasonic;
    private final AnalogInput backUltrasonic = RobotMap.coreBackUltrasonic;
    private final PowerDistributionPanel powerPanel = RobotMap.corePowerPanel;
    private final Compressor compressor = RobotMap.coreCompressor;
    private final DigitalInput gearSensor = RobotMap.coreGearSensor;
    private final Solenoid lEDRing = RobotMap.coreLEDRing;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    private AHRS navx = RobotMap.navx;
    private double lastTimeCompressorWasOn = 0;
    private double availableAirUnits = 0;
    private boolean previousCompressorStatus = false;
    private boolean compressorDisabled = false;
    private double compressorDisabledAt;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Core() {
        navx.zeroYaw();

        DataLogger.addDataElement("Match Time", () -> DriverStation.getInstance().getMatchTime());
        DataLogger.addDataElement("Current Warning", () -> powerPanel.getTotalCurrent());
        DataLogger.addDataElement("AccelX", () -> navx.getWorldLinearAccelX());
        DataLogger.addDataElement("AccelY", () -> navx.getWorldLinearAccelY());
        DataLogger.addDataElement("AccelZ", () -> navx.getWorldLinearAccelZ());
        DataLogger.addDataElement("Voltage", () -> powerPanel.getVoltage());
    }

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onLoop() {
        boolean status = compressor.enabled();

        if (status) {
            setLastTimeCompressorWasOn(Timer.getFPGATimestamp());
        } else if (this.previousCompressorStatus && compressor.getClosedLoopControl()) {
            availableAirUnits = Constants.maxAirUnits;
        }

        this.previousCompressorStatus = status;

        if (compressorDisabled
                && (Timer.getFPGATimestamp() - compressorDisabledAt) >= Constants.compressorDisabledDelay) {
            compressorDisabled = false;
        }
        compressor.setClosedLoopControl(!compressorDisabled);
    }

    @Override
    public void onStop() {
    }

    public double getAvailableAirUnits() {
        return availableAirUnits;
    }

    public void useAirUnits(double val) {
        availableAirUnits -= val;
    }

    public void disableCompressor() {
        if (availableAirUnits > Constants.minimumAirReserve) {
            compressorDisabled = true;
            compressorDisabledAt = Timer.getFPGATimestamp();
        }
    }

    public double getUltrasonic(Ultrasonic u) {
        switch (u) {
        case Front:
            return frontUltrasonic.getAverageVoltage();
        case Left:
            return leftUltrasonic.getAverageVoltage();
        case Right:
            return rightUltrasonic.getAverageVoltage();
        case Back:
            return backUltrasonic.getAverageVoltage();
        default:
            return 0;

        }

    }

    public double getLastTimeCompressorWasOn() {
        return lastTimeCompressorWasOn;
    }

    private void setLastTimeCompressorWasOn(double lastTimeCompressorWasOn) {
        this.lastTimeCompressorWasOn = lastTimeCompressorWasOn;
    }

    public boolean gearPresent() {
        return !this.gearSensor.get();
    }

    public PowerDistributionPanel getPDP() {
        return powerPanel;
    }

    public void turnOnLED() {
        this.lEDRing.set(true);
    }

    public void turnOffLED() {
        lEDRing.set(false);
    }
    
    public SerialPort blingPort = new SerialPort(9600, SerialPort.Port.kMXP);
    
    public void sendLEDMessage(int msg)
    {
    	blingPort.write(ByteBuffer.allocate(4).putInt(msg).array(), 4);
    	blingPort.flush();
    }
    
    public void rainbowLED()
    {
    	sendLEDMessage(1);
    }
    
    public void purpleLED()
    {
    	sendLEDMessage(2);
    }
    
    public void greenLED()
    {
    	sendLEDMessage(3);
    }
    
    public void orangeAndBlueLED()
    {
    	sendLEDMessage(4);
    }
}
