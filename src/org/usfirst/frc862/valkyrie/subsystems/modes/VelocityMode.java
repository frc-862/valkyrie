package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;

public class VelocityMode extends SubsystemMode {

    @Override
    public void onStart() {
        super.onStart();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> { 
            t.changeControlMode(CANTalon.TalonControlMode.Speed);
            t.setPID(0.1, 0, 0);
            t.setF(3.41 / 4);
           
        });
    }

    @Override
    public void teleop(double left, double right) {
        Robot.driveTrain.set(-left * Constants.maxVelocity, -right * Constants.maxVelocity);
    }
}
