package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.CANTalon;

public class BrakeMode extends SubsystemMode {

    public BrakeMode(DriveTrain driveTrain) {
        super(driveTrain);
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        super.initialize();
        drive.eachPrimaryMotor((CANTalon t) -> {
            t.setPID(Constants.brakeP, Constants.brakeI, Constants.brakeD,
                    Constants.brakeIZone, Constants.brakeRampRate,
                    Constants.brakeF, Constants.brakeSlot);
        });
    }

    @Override
    public void end() {
        // TODO Auto-generated method stub
        super.end();
    }

    @Override
    public void teleop(double left, double right) {
        // TODO Auto-generated method stub
        super.teleop(left, right);
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        super.stop();
    }

}
