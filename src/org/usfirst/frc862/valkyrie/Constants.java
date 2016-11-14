package org.usfirst.frc862.valkyrie;

import org.usfirst.frc862.util.ConstantsBase;
import org.usfirst.frc862.util.InterpolatedMap;

public class Constants extends ConstantsBase {
    // Volts per second
    public static double driveRampRate = 12.0;
    public static int encoderTicksPerRev = 360;
    public static double minimumShiftDelay = 0.75;
    public static InterpolatedMap lookupTable;
    
    public String getFileName() {
        return "~/valkyrie.yaml";
    }

    static {
        new Constants().readFromFile();
    }

    public static void main(String[] argv) {
//      Constants.lookupTable = new InterpolatedMap();
//      Constants.lookupTable.put(1.0, 1.0);
//      Constants.lookupTable.put(50.0, 2500.0);
//      Constants.lookupTable.put(100.0, 10000.0);
//      
//      new Constants().writeToFile();

     System.out.println("driveRampRate: " + driveRampRate);
      System.out.println("encoderTicksPerRev: " + encoderTicksPerRev);
      System.out.println("minimumShiftDelay: " + minimumShiftDelay);
      System.out.println("lookupTable: " + lookupTable.toString());
    }
}
