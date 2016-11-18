package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class OpenLoopMode extends SubsystemMode {
    
    public OpenLoopMode(DriveTrain dt) {
        super(dt);
    }
    
    @Override
    public void initialize() {
        drive.eachPrimaryMotor((CANTalon talon) -> {
           talon.changeControlMode(TalonControlMode.PercentVbus); 
        });        
    }
    
    @Override
    public void teleop(double left, double right) {
        drive.set(left, right);
    }
}
