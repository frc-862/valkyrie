package org.usfirst.frc862.valkyrie.subsystems.modes;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;

import com.ctre.CANTalon;

public class BrakeMode extends SubsystemMode {

    @Override
    public void onStart() {
        super.onStart();
        
        Robot.driveTrain.eachPrimaryMotor((CANTalon t) -> {
            t.enableBrakeMode(false);
            t.setPID(Constants.brakeP, Constants.brakeI, Constants.brakeD,
                    Constants.brakeIZone, Constants.brakeRampRate,
                    Constants.brakeF, Constants.brakeSlot);
            t.set(0);
        });
    }
}
