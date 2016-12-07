package org.usfirst.frc862.valkyrie.triggers;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.Trigger;
import org.usfirst.frc862.util.MonitorTrigger;

/**
 * Created by phurley on 12/7/16.
 */
public class LowVoltageTrigger extends MonitorTrigger {
    public LowVoltageTrigger(PowerDistributionPanel panel, double limit, double duration) {
        super(duration, () -> panel.getVoltage() < limit);
    }
}
