package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;

public class SubsystemMode  {
    public void onStart() {
        Logger.debug("start: " + this.getClass().getSimpleName());
    }
    
    public void onLoop() {}
    
    public void onStop() {
        Logger.debug("stop: " + this.getClass().getSimpleName());
    }

    public void teleop(double left, double right) {}
    
    public void upShift() {}
    public void downShift() {}
}
