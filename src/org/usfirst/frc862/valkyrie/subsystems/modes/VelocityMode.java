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
            t.setPID(0.02, 0, 0);
            t.setF(Constants.velocityFeedForward);
            t.enableBrakeMode(false);
        });
        
        Robot.driveTrain.eachSlaveMotor((CANTalon t) -> {
            t.enableBrakeMode(false);
        });
    }
    
    @Override
    public void teleop(double left, double right) {
        // Joysticks are backwards -- forward is negative, positive is backwards
        // But our master is now reversed in a 6cim setup, so no negation
        SmartDashboard.putNumber("Left set point", left * Constants.maxVelocity);
        SmartDashboard.putNumber("Right set point", right * Constants.maxVelocity);
        Robot.driveTrain.set(left * Constants.maxVelocity, right * Constants.maxVelocity);
    }
}
