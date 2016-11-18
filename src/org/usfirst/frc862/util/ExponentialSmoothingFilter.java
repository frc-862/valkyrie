package org.usfirst.frc862.util;

public class ExponentialSmoothingFilter {
    boolean first_time = true;
    double previous;
    double smoothing_factor;
    double reverse_factor;
    
    public ExponentialSmoothingFilter(double smoothing_factor) {
        this.smoothing_factor = smoothing_factor;
        reverse_factor = 1 - smoothing_factor;
        previous = 0;
    }
    
    public double filter(double value) {
        if (first_time) {
            first_time = false;
            previous = value;
        } else {
            previous = smoothing_factor * value + reverse_factor * previous;
        }
        return previous;
    }
}
