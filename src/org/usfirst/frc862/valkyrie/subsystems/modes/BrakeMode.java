package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import edu.wpi.first.wpilibj.CANTalon;

public class BrakeMode extends SubsystemMode {

    @Override
    public void start() {
        super.start();

        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> {
            t.setPID(Constants.brakeP, Constants.brakeI, Constants.brakeD,
                    Constants.brakeIZone, Constants.brakeRampRate,
                    Constants.brakeF, Constants.brakeSlot);
            t.set(0);
        });
    }
}
