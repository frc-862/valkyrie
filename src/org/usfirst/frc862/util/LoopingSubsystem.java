package org.usfirst.frc862.util;

import org.usfirst.frc862.valkyrie.Constants;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class LoopingSubsystem extends Subsystem {
    protected Thread looper;
    protected boolean running = false;
    protected double loop_period;
    protected Notifier notifier;
    
    public LoopingSubsystem(double loop_period) {
        this.loop_period = loop_period;
    }
    
    public void start() {
        running = true;
        looper = new Thread(() -> {
            double start = Timer.getFPGATimestamp();
            double finish = start;
            
            init();
            while (running) {
                start = Timer.getFPGATimestamp();
                loop(start - finish);
                finish = Timer.getFPGATimestamp();
                
                // Not sure why, but delay was off by ~90ms 
                // too short, if it was too long, it could be 
                // the time to setup the delay, will need to 
                // monitor as the CPU usage goes up and we 
                // add more communication overhead
                Timer.delay(loop_period - (finish - start) + Constants.loopTimingFudgeFactor);
            }
            end();
        });
        looper.start();
    }
    
    public void init() { }
    public abstract void loop(double delta);
    public void end() { }
}
