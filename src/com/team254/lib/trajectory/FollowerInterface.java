package com.team254.lib.trajectory;

public interface FollowerInterface {

    void configure(double kp, double ki, double kd, double kv, double ka);

    void reset();

    void setTrajectory(Trajectory profile);

    Trajectory.Segment getSegment();

    double calculate(double distance_so_far);

    double getHeading();

    boolean isFinishedTrajectory();

}