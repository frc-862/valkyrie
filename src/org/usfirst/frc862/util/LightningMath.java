package org.usfirst.frc862.util;

public class LightningMath {

    public static double limit(double v, double low, double high) {
        return (v < low) ? low : ((v > high) ? high : v);
    }

    public static double limit(double v, double limit) {
        return limit(v, -limit, limit);
    }

    public static double boundThetaNegPiToPi(double theta) {
        return theta - (Math.ceil((theta + Math.PI) / (Math.PI * 2)) - 1) * (Math.PI * 2); // (-π;π]
    }

    public static double boundTheta0To2Pi(double theta) {
        return theta - Math.floor(theta / (Math.PI * 2)) * (Math.PI * 2); // [0;2π)
    }

    public static double boundThetaNeg180to180(double theta) {
        return theta - (Math.ceil((theta + 180)/360)-1)*360; // (-180;180]
    }
    
    public static double boundTheta0to360(double theta) {
        return theta - Math.floor(theta/360)*360;  // [0;360)
    }
    
    public static double deltaThetaInDegrees(double from, double to) {
        return boundThetaNeg180to180(to - from);
    }

    public static double deltaThetaInRadians(double from, double to) {
        return boundThetaNegPiToPi(to - from);
    }
    
    public static int scale(int input,
            int lowInput, int highInput, int lowOutput, int highOutput)
    {
        final int inputRange = highInput - lowInput;
        final int outputRange = highOutput - lowOutput;
        
        return (input - lowInput) * outputRange / inputRange + lowOutput;
    }

    public static double scale(double input,
            double lowInput, double highInput, double lowOutput, double highOutput)
    {
        final double inputRange = highInput - lowInput;
        final double outputRange = highOutput - lowOutput;
        
        return  (input - lowInput) * outputRange / inputRange + lowOutput;
    }

    public static double deadZone(double input, double deadband)
    {
        return Math.abs(input) >= deadband ? input : 0;
    }
}
