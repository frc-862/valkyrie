package org.usfirst.frc862.valkyrie.triggers;

import org.usfirst.frc862.util.MonitorTrigger;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by phurley on 12/7/16.
 */
public class LosingPushingBattleTrigger extends MonitorTrigger {
    public LosingPushingBattleTrigger(DriveTrain dt, double duration) {
        // TODO return true if commanded power above calibration and
        //      velocity is decreasing (how to know?)
        super(duration, () -> {
            return false;
            // return (Math.abs(dt.getRequestedPower()) > Constants.PushingHardPower);
        });
    }
}
