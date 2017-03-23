package org.usfirst.frc862.util;

import java.util.Collections;
import java.util.Vector;

import com.team254.lib.trajectory.Trajectory;

public class RecordedPath {
    public class Segment {
        public double left_pos;
        public double right_pos;
        public double heading;
        public double time;
        
        public Segment(double l, double r, double h, double t) {
            left_pos = l;
            right_pos = r;
            heading = h;
            time = t;
        }
    }
    Vector<Segment> path = new Vector<Segment>();

    public RecordedPath() {
        path = new Vector<Segment>();
    }

    public void add(double l, double r, double h, double t) {
        path.add(new Segment(l, r, h, t));
    }
    
    public void reverse() {
        // TODO reverse the path
        Collections.reverse(path);
    }
    
    public Trajectory.Pair convertTrajectory() {
        return convertTrajectory(1.0);
    }
    
    public Trajectory.Pair convertTrajectory(double speed_factor) {
        // TODO generate vel/acc/jerk/x/y from path
        return null;
    }
}
