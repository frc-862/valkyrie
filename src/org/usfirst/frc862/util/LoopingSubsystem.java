package org.usfirst.frc862.util;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class LoopingSubsystem extends Subsystem {
    protected Thread looper;
    protected boolean running = false;
    protected double loop_period;
    
    public LoopingSubsystem(double loop_period) {
        this.loop_period = loop_period;
    }
    
    public void start() {
        running = true;
        looper = new Thread(() -> {
            double start = Timer.getFPGATimestamp();
            init();
            while (running) {
                start = Timer.getFPGATimestamp();
                loop();
                Timer.delay(loop_period - (Timer.getFPGATimestamp() - start));
            }
            end();
        });
        looper.start();
    }
    
    public void init() { }
    public abstract void loop();
    public void end() { }
}
