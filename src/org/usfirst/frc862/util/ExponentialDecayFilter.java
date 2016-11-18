package org.usfirst.frc862.util;

public class ExponentialDecayFilter {
    boolean first_time = true;
    double previous;
    double exponent;
    
    public ExponentialDecayFilter(double exponent) {
        this.exponent = exponent;
        previous = 0;
    }
    
    public double filter(double value) {
        if (first_time) {
            first_time = false;
            previous = value;
            return value;
        } else {
            double delta = value - previous;
            double adjust = Math.pow(Math.abs(delta), exponent);
            if (delta < 0) adjust = -adjust;
            previous = previous + adjust;
            return previous;
        }
    }
}
