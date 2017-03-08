package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

public class OpenLoopMode extends SubsystemMode {   
    @Override
    public void onStart() {
        super.onStart();

        Robot.driveTrain.eachPrimaryMotor((CANTalon talon) -> {
           talon.changeControlMode(TalonControlMode.PercentVbus);
           talon.setVoltageRampRate(Constants.driveRampRate);
        });        
    }
    
    @Override
    public void teleop(double left, double right) {
        Robot.driveTrain.set(left, -right);
    }
    
    @Override
    public void onLoop() {
    }

    @Override
    public void onStop() {
        Robot.driveTrain.set(0, 0);
    }
}
