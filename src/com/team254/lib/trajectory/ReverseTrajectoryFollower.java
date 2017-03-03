package com.team254.lib.trajectory;

/**
 * PID + Feedforward controller for following a Trajectory.
 *
 * @author Jared341
 */
public class ReverseTrajectoryFollower  implements FollowerInterface {

  private double kp_;
  // private double ki_;  // Not currently used, but might be in the future.
  private double kd_;
  private double kv_;
  private double ka_;
  private double last_error_;
  private double current_heading = 0;
  private int current_segment;
  private double initial_position;
  private Trajectory profile_;

  public ReverseTrajectoryFollower() {

  }

  public void configure(double kp, double ki, double kd, double kv, double ka) {
    kp_ = kp;
    // ki_ = ki;
    kd_ = kd;
    kv_ = kv;
    ka_ = ka;
  }

  public void reset() {
    last_error_ = 0.0;
    current_segment = profile_.getNumSegments() - 1;
    initial_position = profile_.getSegment(current_segment).pos;
  }

  public void setTrajectory(Trajectory profile) {
    profile_ = profile;
  }

  public Trajectory.Segment getSegment() {
      return profile_.getSegment(current_segment);
  }
  
  public double calculate(double distance_so_far) {
    if (current_segment >= 0) {
      Trajectory.Segment segment = profile_.getSegment(current_segment);
      double pos = initial_position - segment.pos;
      double error = pos - distance_so_far;
      double output = kp_ * error + kd_ * ((error - last_error_)
              / segment.dt - segment.vel) + (kv_ * segment.vel
              + ka_ * -segment.acc);

      last_error_ = error;
      current_heading = segment.heading;
      current_segment--;
      //System.out.println("so far: " + distance_so_far + "; output: " + output);
      return -output;
    } else {
      return 0;
    }
  }

  public double getHeading() {
    return current_heading;
  }

  public boolean isFinishedTrajectory() {
    return current_segment < 0;
  }
}
