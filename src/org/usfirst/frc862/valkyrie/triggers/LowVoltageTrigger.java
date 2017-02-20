package org.usfirst.frc862.valkyrie.triggers;

import org.usfirst.frc862.util.MonitorTrigger;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by phurley on 12/7/16.
 */
public class LowVoltageTrigger extends MonitorTrigger {
    public LowVoltageTrigger(PowerDistributionPanel panel, double limit, double duration) {
        super(duration, () -> panel.getVoltage() < limit && SmartDashboard.getBoolean("Use Low Voltage Downshift", true));
        //I need a nap
    }
}
