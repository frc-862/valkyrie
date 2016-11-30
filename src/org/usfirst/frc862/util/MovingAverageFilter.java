package org.usfirst.frc862.util;

@SuppressWarnings("WeakerAccess")
public class MovingAverageFilter {
    private double[] values;
    private double sum = 0;
    private int pos = 0;
    private int count = 0;
    
    public MovingAverageFilter(int boxes) {
        values = new double[boxes];
    }
    
    public double filter(double value) {
        sum += value;
        if (count < values.length) {
            ++count;
            values[pos++] = value;
        } else if (pos < values.length) {
            sum -= values[pos];
            values[pos++] = value;
        } else {
            sum -= values[0];
            values[0] = value;
            pos = 1;
        }
        
        return sum / count;
    }    
}
