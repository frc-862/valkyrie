package org.usfirst.frc862.valkyrie.triggers;

import org.usfirst.frc862.util.MonitorTrigger;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by phurley on 12/7/16.
 */
public class HighCurrentTrigger extends MonitorTrigger {
    public HighCurrentTrigger(PowerDistributionPanel panel, double limit, double duration) {
        super(duration, () -> panel.getTotalCurrent() > limit && SmartDashboard.getBoolean("Use High Current Downshift", true));
    }
}
