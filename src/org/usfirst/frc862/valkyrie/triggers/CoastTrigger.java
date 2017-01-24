package org.usfirst.frc862.valkyrie.triggers;

import org.usfirst.frc862.util.MonitorTrigger;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

/**
 * Created by phurley on 12/7/16.
 */
public class CoastTrigger extends MonitorTrigger {
    public CoastTrigger(DriveTrain dt, double limit, double duration, double minCoastPower) {
        super(duration, () -> {
            return (Math.abs(dt.getAverageVelocity()) < limit) &&
                    (Math.abs(dt.getRequestedPower()) < minCoastPower);
        });
    }
}