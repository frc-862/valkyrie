package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;

import com.ctre.CANTalon;

public class MotionMagicMode extends SubsystemMode {

    @Override
    public void onStart() {
        super.onStart();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> { 
            t.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
            t.enableBrakeMode(false);
            
            t.configNominalOutputVoltage(+0.0f, -0.0f);
            t.configPeakOutputVoltage(+12.0f, -12.0f);
            
            t.setMotionMagicCruiseVelocity(Constants.magicVelocity);
            t.setMotionMagicAcceleration(Constants.magicAcceleration);
        });
        
        Robot.shifter.downShift();
        
        Robot.driveTrain.eachSlaveMotor((CANTalon t) -> {
            t.enableBrakeMode(false);
        });
    }
    
    @Override
    public void teleop(double left, double right) { }
}
