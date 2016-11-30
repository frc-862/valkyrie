package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class OpenLoopMode extends SubsystemMode {   
    private Timer shifting = new Timer();
    private double currentLeftPower = 0;
    private double currentRightPower = 0;
    private double leftPower = 0;
    private double rightPower = 0;
    enum State { NORMAL, UPSHIFTING, DOWNSHIFTING }

    private State state = State.NORMAL;
    
    @Override
    public void start() {
        super.start();

        Robot.driveTrain.eachPrimaryMotor((CANTalon talon) -> {
           talon.changeControlMode(TalonControlMode.PercentVbus);
           talon.setVoltageRampRate(Constants.driveRampRate);
        });        
    }
    
    @Override
    public void teleop(Joystick left, Joystick right) {
        leftPower = left.getRawAxis(0);
        rightPower = right.getRawAxis(0);
    }
    
    @Override
    public void loop(double delta) {
        Logger.debug("loop: " + delta);

        switch (state) {
        case NORMAL:
            break;
            
        case UPSHIFTING:
            if (shifting.get() >= Constants.shiftDelay) {
                state = State.NORMAL;
                RobotMap.driveTrainShifter.set(Value.kForward);
            } else {
                rightPower = 0;
                leftPower = 0;
            }
            break;
            
        case DOWNSHIFTING:
            if (shifting.get() >= Constants.shiftDelay) {
                state = State.NORMAL;
                RobotMap.driveTrainShifter.set(Value.kReverse);
            } else {
                rightPower = 0;
                leftPower = 0;
            }
            break;
        }
        
        double maxChange = Constants.maxRampRate * delta;
        double deltaRight = rightPower - currentRightPower;
        double deltaLeft = leftPower - currentLeftPower;
        
        if (deltaRight > maxChange) deltaRight = maxChange;
        if (deltaRight < -maxChange) deltaRight = -maxChange;
        if (deltaLeft > maxChange) deltaLeft = maxChange;
        if (deltaLeft < -maxChange) deltaLeft = -maxChange;
        
        currentRightPower += deltaRight;
        currentLeftPower += deltaLeft;
        
        if (currentRightPower > 0 && rightPower < 0) currentRightPower = 0;
        if (currentRightPower < 0 && rightPower > 0) currentRightPower = 0;
        if (currentLeftPower > 0 && leftPower < 0) currentLeftPower = 0;
        if (currentLeftPower < 0 && leftPower > 0) currentLeftPower = 0;

        Robot.driveTrain.set(currentRightPower, currentLeftPower);
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        Robot.driveTrain.set(0, 0);
    }

    @Override
    public void upShift() {
        shifting.reset();
        shifting.start();
        state = State.UPSHIFTING;
    }

    @Override
    public void downShift() {
        shifting.reset();
        shifting.start();
        state = State.DOWNSHIFTING;
    }
}
