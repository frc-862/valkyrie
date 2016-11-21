public class KalmanFilter
{
    private double kQ;
    private double kR;
    private double prevP;
    private double prevEstimate;
    private boolean first_time;

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
        double p = (1 - k) * tempP;

        prevP = p;
        prevEstimate = xEst;

        return prevEstimate;
    }

}
