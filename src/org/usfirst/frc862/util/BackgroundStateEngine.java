package org.usfirst.frc862.util;

import edu.wpi.first.wpilibj.Notifier;

public class BackgroundStateEngine extends StateMachine {
    private boolean running = false;
    private Notifier looper;
    private double loop_period;
    
    public BackgroundStateEngine(StateEntry se, double period) {
        super(se);
        loop_period = period;
        looper = new Notifier(() -> {
            if (running) {
                loop();
            }
        });
    }

    public void start() {
        if (!running) {
            running = true;
            super.start();
            looper.startPeriodic(loop_period);
        }        
    }
    
    public void stop() {        
        if (running) {
            looper.stop();
            running = false;
            super.stop();
        }
    }
}
