package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;

import edu.wpi.first.wpilibj.Joystick;

public class SubsystemMode {
    public void start() {
        Logger.debug("start: " + this.getClass().getSimpleName());
    }
    
    public void loop(double delta) {}
    
    public void stop() {
        Logger.debug("stop: " + this.getClass().getSimpleName());
    }

    public void teleop(Joystick left, Joystick right) {}
    
    public void upShift() {}
    public void downShift() {}
}
