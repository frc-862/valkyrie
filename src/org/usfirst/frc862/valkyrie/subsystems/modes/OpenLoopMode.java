package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;

public class OpenLoopMode extends SubsystemMode {    
    @Override
    public void start() {
        Robot.driveTrain.eachPrimaryMotor((CANTalon talon) -> {
           talon.changeControlMode(TalonControlMode.PercentVbus);
           talon.setVoltageRampRate(Constants.driveRampRate);
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
