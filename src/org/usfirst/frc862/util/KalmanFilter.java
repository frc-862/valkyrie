package org.usfirst.frc862.util;

@SuppressWarnings("WeakerAccess")
public class KalmanFilter
{
    private double kQ;
    private double kR;
    private double prevP;
    private double prevEstimate;
    private boolean first_time;

    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    public KalmanFilter(double kQ, double kR)
    {
        this.kQ = kQ;
        this.kR = kR;
        prevP = 0.0;
        prevEstimate = 0.0;
        first_time = true;
    }

    public KalmanFilter()
    {
        this(0.024, 0.6158);
    }

    public double filter(double value)
    {
        if (first_time)
        {
            prevEstimate = value;
            first_time = false;
        }

        double tempP = prevP + kQ;
        double k = tempP / (tempP + kR);
        double xEst = prevEstimate + k * (value - prevEstimate);

        prevP = (1 - k) * tempP;
        prevEstimate = xEst;

        return prevEstimate;
    }

}
