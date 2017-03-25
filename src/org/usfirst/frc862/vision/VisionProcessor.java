package org.usfirst.frc862.vision;

import org.usfirst.frc862.trajectory.RobotState;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.util.Loop;

import edu.wpi.first.wpilibj.Utility;

/**
 * This function adds vision updates (from the Nexus smartphone) to a list in
 * RobotState. This helps keep track of goals detected by the vision system. The
 * code to determine the best goal to shoot at and prune old Goal tracks is in
 * GoalTracker.java
 * 
 * @see PegTracker.java
 */
public class VisionProcessor implements Loop, VisionUpdateReceiver {
    static VisionProcessor instance_ = new VisionProcessor();
    VisionUpdate update_ = null;
    RobotState robot_state_ = RobotState.getInstance();

    public static VisionProcessor getInstance() {
        return instance_;
    }

    VisionProcessor() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onLoop() {
        if (Utility.getUserButton()) {
            Logger.debug("Restart everything");
//            VisionServer.getInstance().restartAdb();
//            VisionServer.getInstance().restartApp();
            VisionServer.getInstance().requestAppRestart();
            Logger.flush();
        }
        VisionUpdate update;
        synchronized (this) {
            if (update_ == null) {
                return;
            }
            update = update_;
            update_ = null;
        }
        for (TargetInfo target : update.targets) {
            Logger.debug("vision update " + target.lat + "," + target.lon + " - " + target.theta);
        }
        robot_state_.addVisionUpdate(update.getCapturedAtTimestamp(), update.getTargets());
    }

    @Override
    public void onStop() {
        // no-op
    }

    @Override
    public synchronized void gotUpdate(VisionUpdate update) {
        Logger.debug("Vision gotUpdate!");
        update_ = update;
    }

}
