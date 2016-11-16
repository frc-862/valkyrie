package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

public class SubsystemMode {
    public DriveTrain drive;
    
    public SubsystemMode(DriveTrain dt) {
        drive = dt;
    }
    
    public void initialize() {}
    
    public void loop() {}
    public void end() {} 
    
    public void teleop(double left, double right) {}
    public void stop() {}
}
