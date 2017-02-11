package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VelocityMode extends SubsystemMode {

    @Override
    public void onStart() {
        super.onStart();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> { 
            t.changeControlMode(CANTalon.TalonControlMode.Speed);
            t.setPID(Constants.velocityPTerm, 0, 0);
            t.enableBrakeMode(false);
        });
        
        Robot.driveTrain.leftPrimaryMotor((CANTalon t) ->{
        	t.setF(Constants.velocityFeedForwardL);
        });
        
        Robot.driveTrain.rightPrimaryMotor((CANTalon t) ->{
        	t.setF(Constants.velocityFeedForwardR);
        });
        
        Robot.driveTrain.eachSlaveMotor((CANTalon t) -> {
            t.enableBrakeMode(false);
        });
    }
    
    @Override
    public void teleop(double left, double right) {
        // Joysticks are backwards -- forward is negative, positive is backwards
        // But our master is now reversed in a 6cim setup, so no negation
        Robot.driveTrain.set(left * -Constants.maxVelocity, right * -Constants.maxVelocity);
    }
}
