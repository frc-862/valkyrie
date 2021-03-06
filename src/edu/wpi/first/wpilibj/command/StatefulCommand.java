package edu.wpi.first.wpilibj.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.usfirst.frc862.util.Logger;

import edu.wpi.first.wpilibj.command.Command;

public class StatefulCommand extends Command {
    private Enum<?> state;
    protected Runnable default_action = () -> {};
    private Enum<?> previous_state = null;
    private Enum<?> calling_state = null;
    
    private HashMap<Enum<?>,Command> commands = new HashMap<Enum<?>,Command>();
    private HashMap<Enum<?>,Supplier<Enum<?>>> next_state = new HashMap<Enum<?>,Supplier<Enum<?>>>();
    
    Command currentCommand = null;
    
    public void setState(Enum<?> new_state) {
        state = new_state;
    }
    
    public Enum<?> getState() {
        return state;
    }
    
    public Enum<?> getCallingState() {
        return calling_state;
    }
    
    protected void registerState(Enum<?> state, Command cmd, Supplier<Enum<?>> next) {
        commands.put(state, cmd);
        next_state.put(state, next);
    }
    
    protected void setDefaultAction(Runnable action) {
        default_action = action;
    }

    public StatefulCommand(Enum<?> state) {
        this.state = state;
    }

    @Override
    protected void initialize() {
        previous_state = null;
        calling_state = state;
        currentCommand = null;
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    private boolean call(String method_name) {
        try {
//            Logger.debug("Call " + method_name);
            Method method = getClass().getMethod(method_name);
            method.invoke(this);
        } catch (NoSuchMethodException | SecurityException | 
                 IllegalAccessException | IllegalArgumentException | 
                 InvocationTargetException e) {
            Logger.error("StatefulCommand missing method: " + method_name);
            return false;
        }        
        return true;
    }
    
    protected String methodName(Enum<?> state) {
        String state_name = state.name().toLowerCase();
        String method_name = Stream.of(state_name.split("[^a-zA-Z0-9]"))
                .map(v -> v.substring(0, 1).toUpperCase() + v.substring(1).toLowerCase())
                .collect(Collectors.joining());
        method_name = method_name.substring(0, 1).toLowerCase() + method_name.substring(1);
        return method_name;
    }
    
    @Override
    protected void execute() {
        if (previous_state != state) {
            if (previous_state != null) {
                String exit_method = methodName(previous_state) + "Exit";
                call(exit_method);
            }
            
            previous_state = state;
            calling_state = state;
            currentCommand = commands.get(state);
            call(methodName(state) + "Enter");
        }
        
        if (currentCommand != null) {
            if (!currentCommand.run()) {
                currentCommand.removed();
                state = next_state.get(state).get();
            }
        } else if (!call(methodName(state))) {
            this.default_action.run();
        }
    }
}
