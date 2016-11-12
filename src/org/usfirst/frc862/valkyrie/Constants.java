package org.usfirst.frc862.valkyrie;

import org.usfirst.frc862.util.ConstantsBase;

public class Constants extends ConstantsBase {
    public static double driveRampDelay = 1.0; // seconds

    public String getFileName() {
        return "~/valkyrie.properties";
    }

    static {
        new Constants().readFromFile();
    }
}
