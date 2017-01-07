package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;

public class VelocityMode extends SubsystemMode {

    @Override
    public void onStart() {
        super.onStart();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> { 
            t.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        });
    }

    @Override
    public void teleop(Joystick left, Joystick right) {
        Robot.driveTrain.set(
                left.getAxis(Joystick.AxisType.kY), 
                right.getAxis(Joystick.AxisType.kY)
        );
    }
}
