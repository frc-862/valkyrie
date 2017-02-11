package org.usfirst.frc862.valkyrie.triggers;

import org.usfirst.frc862.util.MonitorTrigger;
import org.usfirst.frc862.valkyrie.Robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by phurley on 12/7/16.
 */
public class CrashTrigger extends MonitorTrigger {
    public CrashTrigger(AHRS navx, double limit, double duration) {
        // TODO verify Y is the correct axis for velocity
        // TODO expiriment with corner impacts do we need both axis (probably need both)
        super(duration, () -> Math.abs(navx.getRawAccelY()) > limit &&
                !Robot.shifter.hysteresisSafetyNet() &&
                SmartDashboard.getBoolean("Use Crash Downshift", true));
    }
}
