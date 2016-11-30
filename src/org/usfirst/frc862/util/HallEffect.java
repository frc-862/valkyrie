package org.usfirst.frc862.util;

import edu.wpi.first.wpilibj.DigitalInput;

@SuppressWarnings("WeakerAccess")
public class HallEffect extends DigitalInput {

    public HallEffect(int channel) {
        super(channel);
    }

}
