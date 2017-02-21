package org.usfirst.frc862.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.wpi.first.wpilibj.command.Command;

public class StatefulCommand extends Command {
    protected Enum<?> state;
    protected Runnable default_action = () -> {};
    private Enum<?> previous_state = null;
    
    protected void setDefaultAction(Runnable action) {
        default_action = action;
    }

    public StatefulCommand(Enum<?> state) {
        this.state = state;
    }

    @Override
    protected void initialize() {
        previous_state = null;
    }

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean call(String method_name) {
        try {
            Method method = getClass().getMethod(method_name);
            method.invoke(this);
        } catch (NoSuchMethodException | SecurityException | 
                 IllegalAccessException | IllegalArgumentException | 
                 InvocationTargetException e) {
            return false;
        }        
        return true;
    }
    
    private String methodName(Enum<?> state) {
        String state_name = state.name();
        String method_name = Stream.of(state_name.split("[^a-zA-Z0-9]"))
                .map(v -> v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase())
                .collect(Collectors.joining());
        return method_name;
    }
    
    @Override
    protected void execute() {
        if (previous_state  != state) {
            if (previous_state != null) {
                String exit_method = methodName(previous_state) + "Exit";
                call(exit_method);
            }
            
            call(methodName(state) + "Enter");
        }
        
        if (!call(methodName(state))) {
            this.default_action.run();
        }
    }
}
