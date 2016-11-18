package org.usfirst.frc862.util;

public class MovingAverageFilter {
    double[] values;
    double sum = 0;
    int pos = 0;
    int count = 0;
    
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
