package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.RobotMap;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class OpenLoopMode extends SubsystemMode {   
    private Timer shifting = new Timer();
    private double lasttime = 0;
    private double currentLeftPower = 0;
    private double currentRightPower = 0;
    private double leftPower = 0;
    private double rightPower = 0;
    enum State { NORMAL, UPSHIFTING, DOWNSHIFTING }

    private State state = State.NORMAL;
    
    @Override
    public void onStart() {
        super.onStart();

        Robot.driveTrain.eachPrimaryMotor((CANTalon talon) -> {
           talon.changeControlMode(TalonControlMode.PercentVbus);
           talon.setVoltageRampRate(Constants.driveRampRate);
        });        
        
        lasttime = Timer.getFPGATimestamp();
    }
    
    @Override
    public void teleop(Joystick left, Joystick right) {
        leftPower = left.getRawAxis(1);
        rightPower = right.getRawAxis(1);
        Robot.driveTrain.set(leftPower, rightPower);
    }
    
    @Override
    public void onLoop() {
    }

    @Override
    public void onStop() {
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
