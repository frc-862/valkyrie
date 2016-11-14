package org.usfirst.frc862.valkyrie;

import org.usfirst.frc862.util.ConstantsBase;

public class Constants extends ConstantsBase {
    // Volts per second
    public static double driveRampRate = 12.0;
    public static int encoderTicksPerRev = 360;
    public static double minimumShiftDelay = 0.75;

    public String getFileName() {
        return "~/valkyrie.yaml";
    }

    static {
        new Constants().readFromFile();
    }

    // public static void main(String[] argv) {
    // new Constants().writeToFile();

    // System.out.println("driveRampRate: " + driveRampRate);
    // System.out.println("encoderTicksPerRev: " + encoderTicksPerRev);
    // System.out.println("minimumShiftDelay: " + minimumShiftDelay);
    // }
}
