package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;

import com.ctre.CANTalon;

public class VelocityMode extends SubsystemMode {

    @Override
    public void onStart() {
        super.onStart();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> { 
            t.changeControlMode(CANTalon.TalonControlMode.Speed);
            
            t.enableBrakeMode(false);
        });
        
        if(Robot.shifter.isLowGear()){
            Robot.driveTrain.configureLowGear();
        }
        else{
            Robot.driveTrain.configureHighGear();
        }
        
        Robot.driveTrain.eachSlaveMotor((CANTalon t) -> {
            t.enableBrakeMode(false);
        });
    }
    
    @Override
    public void teleop(double left, double right) {
//        Robot.driveTrain.set(left * - Robot.shifter.getMaxVelocity(), right * -Robot.shifter.getMaxVelocity());
        Robot.driveTrain.set(left * -Constants.maxVelocityHigh, right * -Constants.maxVelocityHigh);
    }
}
