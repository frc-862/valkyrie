package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.ExponentialSmoothingFilter;
import org.usfirst.frc862.util.Logger;
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
    
    ExponentialSmoothingFilter f = new ExponentialSmoothingFilter(0.1);
    private long counter = 0;
    
    public void loop(double delta) {
        delta = f.filter(delta);
        if (counter++ % 100 == 0)
            Logger.debug("Delta: " + delta);
    }
}
