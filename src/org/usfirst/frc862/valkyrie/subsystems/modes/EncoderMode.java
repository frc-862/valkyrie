package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Robot;

import com.ctre.CANTalon;

public class EncoderMode extends SubsystemMode {

    @Override
    public void onStart() {
        super.onStart();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> { 
            t.changeControlMode(CANTalon.TalonControlMode.Position);
            t.setPID(1.0 / 360.0, 0, 0);
            t.setF(0);
        });
    }
    
    @Override
    public void teleop(double left, double right) {
        // this is a no-op, we don't use joysticks to control talons in position mode
    }
}
