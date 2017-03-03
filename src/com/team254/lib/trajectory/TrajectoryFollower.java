package com.team254.lib.trajectory;

/**
 * PID + Feedforward controller for following a Trajectory.
 *
 * @author Jared341
 */
public class TrajectoryFollower implements FollowerInterface {

  private double kp_;
  // private double ki_;  // Not currently used, but might be in the future.
  private double kd_;
  private double kv_;
  private double ka_;
  private double last_error_;
  private double current_heading = 0;
  private int current_segment;
  private Trajectory profile_;

  public TrajectoryFollower() {

  }

  /* (non-Javadoc)
 * @see com.team254.lib.trajectory.FollowerInterface#configure(double, double, double, double, double)
 */
@Override
public void configure(double kp, double ki, double kd, double kv, double ka) {
    kp_ = kp;
    // ki_ = ki;
    kd_ = kd;
    kv_ = kv;
    ka_ = ka;
  }

  /* (non-Javadoc)
 * @see com.team254.lib.trajectory.FollowerInterface#reset()
 */
@Override
public void reset() {
    last_error_ = 0.0;
    current_segment = 0;
  }

  /* (non-Javadoc)
 * @see com.team254.lib.trajectory.FollowerInterface#setTrajectory(com.team254.lib.trajectory.Trajectory)
 */
@Override
public void setTrajectory(Trajectory profile) {
    profile_ = profile;
  }

  /* (non-Javadoc)
 * @see com.team254.lib.trajectory.FollowerInterface#getSegment()
 */
@Override
public Trajectory.Segment getSegment() {
      return profile_.getSegment(current_segment);
  }
  
  /* (non-Javadoc)
 * @see com.team254.lib.trajectory.FollowerInterface#calculate(double)
 */
@Override
public double calculate(double distance_so_far) {
    if (current_segment < profile_.getNumSegments()) {
      Trajectory.Segment segment = profile_.getSegment(current_segment);
      double error = segment.pos - distance_so_far;
      double output = kp_ * error + kd_ * ((error - last_error_)
              / segment.dt - segment.vel) + (kv_ * segment.vel
              + ka_ * segment.acc);

      last_error_ = error;
      current_heading = segment.heading;
      current_segment++;
      //System.out.println("so far: " + distance_so_far + "; output: " + output);
      return output;
    } else {
      return 0;
    }
  }

  /* (non-Javadoc)
 * @see com.team254.lib.trajectory.FollowerInterface#getHeading()
 */
@Override
public double getHeading() {
    return current_heading;
  }

  /* (non-Javadoc)
 * @see com.team254.lib.trajectory.FollowerInterface#isFinishedTrajectory()
 */
@Override
public boolean isFinishedTrajectory() {
    return current_segment >= profile_.getNumSegments();
  }
}
