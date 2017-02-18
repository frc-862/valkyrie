package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Loop;
import org.usfirst.frc862.valkyrie.Robot;

public class TestMode extends SubsystemMode {
    public void onStart() {
        Logger.debug("start: " + this.getClass().getSimpleName());
        Robot.driveTrain.configure_test_mode();
    }
    
    public void onLoop() {}
    
    public void onStop() {
        Logger.debug("stop: " + this.getClass().getSimpleName());
        Robot.driveTrain.configure_follow_modes();
    }

    public void teleop(double left, double right) {}
    
    public void upShift() {}
    public void downShift() {}
}
