package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class OpenLoopDrive extends SubsystemMode {
    
    public OpenLoopDrive(DriveTrain dt) {
        super(dt);
    }
    
    @Override
    public void initialize() {
        drive.eachPrimaryMotor((CANTalon talon) -> {
           talon.changeControlMode(TalonControlMode.PercentVbus); 
        });        
    }
}
