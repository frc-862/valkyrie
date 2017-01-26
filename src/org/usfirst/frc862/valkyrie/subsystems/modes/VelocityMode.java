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
    public void onLoop() {
        // TODO Auto-generated method stub
        super.onLoop();
        if (Robot.driveTrain.getDrivingStraight()) {
            // TODO -- apply proportional power adjustment based on drive theta
            // TODO -- figure out best way to expose more of drive train to sub-modes
        }
    }


    @Override
    public void teleop(double left, double right) {
        // Joysticks are backwards -- forward is negative, positive is backwards
        
        if (Math.abs(left - right) < Constants.straightCommandDelta) {
            Robot.driveTrain.setDrivingStraight(true);
            
            double power = (left + right) / -2.0;
            Robot.driveTrain.set(power, power);
        } else {
            Robot.driveTrain.setDrivingStraight(false); 
            Robot.driveTrain.set(-left * Constants.maxVelocity, -right * Constants.maxVelocity);
        }
    }
}
