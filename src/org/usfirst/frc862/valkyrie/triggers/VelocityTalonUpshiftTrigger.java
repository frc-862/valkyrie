package org.usfirst.frc862.valkyrie.triggers;

import org.usfirst.frc862.util.MonitorTrigger;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by phurley on 12/7/16.
 */
public class VelocityTalonUpshiftTrigger extends MonitorTrigger {
    public VelocityTalonUpshiftTrigger(DriveTrain dt, double powerLimit, double velocityLimit, double duration) {
        super(duration, () -> (dt.getRequestedPower() > powerLimit) &&
                (dt.getAbsVelocity() > velocityLimit) && 
                SmartDashboard.getBoolean("Use Velocity Upshift", true));
    }
}
