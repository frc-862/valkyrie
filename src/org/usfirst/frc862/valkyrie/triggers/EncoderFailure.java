package org.usfirst.frc862.valkyrie.triggers;

import org.usfirst.frc862.util.MonitorTrigger;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Created by phurley on 12/7/16.
 */

// TODO Explain this to me, because I don't get it :-)
public class EncoderFailure extends MonitorTrigger {
    public EncoderFailure(PowerDistributionPanel panel, double limit, double duration) {
        super(duration, () -> panel.getTotalCurrent() > limit);
    }
}
