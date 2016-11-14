package org.usfirst.frc862.util;

import java.util.TreeMap;

public class InterpolatedMap extends TreeMap<Double,Double> {
    private static final long serialVersionUID = 8077601264655543291L;

    public InterpolatedMap() {
        super();
    }
    
    public double get(double key) {
        Double l = super.get(key);
        
        if (l == null) {
            Double floorKey = super.floorKey(key);
            Double ceilKey = super.ceilingKey(key);
            double range = ceilKey - floorKey;
            double percent = (key - floorKey) / range;
            double floor = super.get(floorKey);
            l = (super.get(ceilKey) - floor) * percent + floor;
        }
        
        return l;
    }
}
