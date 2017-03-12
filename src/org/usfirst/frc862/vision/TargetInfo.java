package org.usfirst.frc862.vision;

/**
 * A container class for Targets detected by the vision system, containing the
 * location in three-dimensional space.
 */
public class TargetInfo {
    protected double x = 1.0;
    protected double y;
    protected double z;
    protected double lon;
    protected double lat;
    protected double theta;
    protected int type;

    public TargetInfo(double y, double z, double lon, double lat, double theta, int type) {
        this.y = y;
        this.z = z;
        this.lon = lon;
        this.lat = lat;
        this.theta = theta;
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getLongitudinalDistance() {
        return lon;
    }

    public double getLatidunalDistance() {
        return lat;
    }

    public double getTheta() {
        return theta;
    }

    public int getType() {
        return type;
    }
}