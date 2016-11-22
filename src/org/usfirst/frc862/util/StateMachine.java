package org.usfirst.frc862.util;

import java.util.HashMap;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class StateMachine {
    public class State {
        String name;
        
        public void enter() {}
        public int loop(double delta) { return 0; }
        public void exit() {}
    }
    
    public class StateEntry {
        public State state;
        public HashMap<Integer,StateEntry> transitions = new HashMap<>();
    }
    
    boolean first_time = true;
    StateEntry currentState;
    double start;
    double finish;
    
    public StateMachine(StateEntry se) {
        currentState = se;
        finish = Timer.getFPGATimestamp();
    }
    
    public void start() {
        currentState.state.enter();
    }
    
    public void loop() {
        start = Timer.getFPGATimestamp();
        int rc = currentState.state.loop(start - finish);
        finish = Timer.getFPGATimestamp();
        
        StateEntry se = currentState.transitions.get(rc);
        if (se != null && se != currentState) {
            currentState.state.exit();
            currentState = se;
            currentState.state.enter();
        }
    }
    
    void stop() {
        currentState.state.exit();
    }
}
