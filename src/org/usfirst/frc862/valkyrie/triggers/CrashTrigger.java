package org.usfirst.frc862.valkyrie.triggers;

import org.usfirst.frc862.util.MonitorTrigger;

import com.kauailabs.navx.frc.AHRS;

/**
 * Created by phurley on 12/7/16.
 */
public class CrashTrigger extends MonitorTrigger {
    public CrashTrigger(AHRS navx, double limit, double duration) {
        super(duration, () -> Math.abs(navx.getRawAccelY()) > limit);
    }
}
