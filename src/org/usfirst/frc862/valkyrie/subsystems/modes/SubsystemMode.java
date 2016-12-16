package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Loop;

import edu.wpi.first.wpilibj.Joystick;

public class SubsystemMode  {
    public void onStart() {
        Logger.debug("start: " + this.getClass().getSimpleName());
    }
    
    public void onLoop() {}
    
    public void onStop() {
        Logger.debug("stop: " + this.getClass().getSimpleName());
    }

    public void teleop(Joystick left, Joystick right) {}
    
    public void upShift() {}
    public void downShift() {}
}
