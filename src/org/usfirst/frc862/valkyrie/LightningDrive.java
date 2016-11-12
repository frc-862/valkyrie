package org.usfirst.frc862.valkyrie;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

public class LightningDrive extends RobotDrive {
    private CANTalon left1, left2, right1, right2;
    private DoubleSolenoid shifter;
    private double last_shift = Timer.getFPGATimestamp();

    public LightningDrive(CANTalon left1, CANTalon left2, CANTalon right1, CANTalon right2, DoubleSolenoid shifter) {
        super(left1, left2, right1, right2);

        this.left1 = left1;
        this.left2 = left2;
        this.right1 = right1;
        this.right2 = right2;
        this.shifter = shifter;

        this.left2.changeControlMode(TalonControlMode.Follower);
        this.left2.set(left1.getDeviceID());

        this.right2.changeControlMode(TalonControlMode.Follower);
        this.right2.set(right1.getDeviceID());
        
        eachMotor((CANTalon m) -> {
            m.setVoltageRampRate(Constants.driveRampRate);
            m.configEncoderCodesPerRev(Constants.encoderTicksPerRev);
        });
    }

    public void eachMotor(Consumer<CANTalon> func) {
        func.accept(left1);
//        func.accept(left2);
        func.accept(right1);
//        func.accept(right2);
    }

    public boolean shouldDownShift() {
        // if throttle is below cal & speed is below cal
        // if throttle is above cal & speed is falling (pushing battle)
        // if speed is below cal and we hit something (jerk above cal)
        return false;
    }

    public boolean shouldUpShift() {
        // if throttle is above cal & speed is above cal
        // ?
        return false;
    }

    public void upShift() {
        shifter.set(Value.kForward);
        last_shift = Timer.getFPGATimestamp();
    }

    public void downShift() {
        shifter.set(Value.kReverse);
        last_shift = Timer.getFPGATimestamp();
    }

    public void autoShift() {
        if (Timer.getFPGATimestamp() - last_shift > Constants.minimumShiftDelay) {
            if (shouldUpShift()) {
                upShift();
            } else if (shouldDownShift()) {
                downShift();
            }
        }
    }
}
