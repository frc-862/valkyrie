package org.usfirst.frc862.util;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class LoopingSubsystem extends Subsystem {
    protected boolean running = false;
    protected double loop_period;
    protected Notifier looper;
    
    private double start;
    private double finish;

    public LoopingSubsystem(double loop_period) {
        this.loop_period = loop_period;
        looper = new Notifier(() -> {
            if (running) {
                start = Timer.getFPGATimestamp();
                loop(start - finish);
                finish = Timer.getFPGATimestamp();
            }
        });
    }

    public synchronized void start_loop() {
        if (!running) {
            running = true;
            start = 0;
            finish = start;

            init();
            looper.startPeriodic(loop_period);
        }
    }

    public synchronized void stop_loop() {
        if (running) {
            looper.stop();
            running = false;
            end();
        }
    }

    public void init() {
    }

    public abstract void loop(double delta);

    public void end() {
    }
}
