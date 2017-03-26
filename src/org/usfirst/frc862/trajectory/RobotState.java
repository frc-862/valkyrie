package org.usfirst.frc862.trajectory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.usfirst.frc862.trajectory.PegTracker.TrackReport;
import org.usfirst.frc862.trajectory.RigidTransform2d.Delta;
import org.usfirst.frc862.util.LightningMath;
import org.usfirst.frc862.util.Logger;
import org.usfirst.frc862.vision.TargetInfo;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * RobotState keeps track of the poses of various coordinate frames throughout
 * the match. A coordinate frame is simply a point and direction in space that
 * defines an (x,y) coordinate system. Transforms (or poses) keep track of the
 * spatial relationship between different frames.
 * 
 * Robot frames of interest (from parent to child):
 * 
 * 1. Field frame: origin is where the robot is turned on
 * 
 * 2. Vehicle frame: origin is the center of the robot wheelbase, facing
 * forwards
 * 
 * 3. Turret fixed frame: origin is the center of the turret when the turret is
 * at 0 degrees rotation relative to the vehicle frame
 * 
 * 4. Turret rotating frame: origin is the center of the turret as it rotates
 * 
 * 5. Camera frame: origin is the center of the camera imager as it rotates with
 * the turret
 * 
 * 6. Goal frame: origin is the center of the goal (note that orientation in
 * this frame is arbitrary). Also note that there can be multiple goal frames.
 * 
 * As a kinematic chain with 6 frames, there are 5 transforms of interest:
 * 
 * 1. Field-to-vehicle: This is tracked over time by integrating encoder and
 * gyro measurements. It will inevitably drift, but is usually accurate over
 * short time periods.
 * 
 * 2. Vehicle-to-turret-fixed: This is a constant.
 * 
 * 3. Vehicle-to-turret-rotating: This is a pure rotation, and is tracked over
 * time using the turret encoder.
 * 
 * 4. Turret-rotating-to-camera: This is a constant.
 * 
 * 5. Camera-to-goal: This is a pure translation, and is measured by the vision
 * system.
 */

public class RobotState {
    private static RobotState instance_ = new RobotState();

    public static RobotState getInstance() {
        return instance_;
    }

    public static final int kObservationBufferSize = 100;
    public static final double kMaxTargetAge = 0.4;

//    public static final RigidTransform2d kVehicleToTurretFixed = new RigidTransform2d(
//            new Translation2d(Constants.kTurretXOffset, Constants.kTurretYOffset),
//            Rotation2d.fromDegrees(Constants.kTurretAngleOffsetDegrees));
//
//    public static final RigidTransform2d kTurretRotatingToCamera = new RigidTransform2d(
//            new Translation2d(Constants.kCameraXOffset, Constants.kCameraYOffset), new Rotation2d());
//
//    // FPGATimestamp -> RigidTransform2d or Rotation2d
    protected InterpolatingTreeMap<InterpolatingDouble, RigidTransform2d> field_to_vehicle_;
    protected RigidTransform2d.Delta vehicle_velocity_;
//    protected InterpolatingTreeMap<InterpolatingDouble, Rotation2d> turret_rotation_;
//    protected GoalTracker goal_tracker_;
    protected PegTracker peg_tracker;
    protected LoadingStationTracker loading_station_tracker;
//    protected Rotation2d camera_pitch_correction_;
//    protected Rotation2d camera_yaw_correction_;
//    protected double differential_height_;

    protected RobotState() {
        reset(0, new RigidTransform2d(), new Rotation2d());
    }

    public synchronized void reset(double start_time, RigidTransform2d initial_field_to_vehicle,
            Rotation2d initial_turret_rotation) {
        field_to_vehicle_ = new InterpolatingTreeMap<>(kObservationBufferSize);
        field_to_vehicle_.put(new InterpolatingDouble(start_time), initial_field_to_vehicle);
        vehicle_velocity_ = new RigidTransform2d.Delta(0, 0, 0);
        peg_tracker = new PegTracker();
        loading_station_tracker = new LoadingStationTracker();
        
        // camera is providing offset data, so we will not transform here
//        camera_pitch_correction_ = Rotation2d.fromDegrees(-Constants.kCameraPitchAngleDegrees);
//        camera_yaw_correction_ = Rotation2d.fromDegrees(-Constants.kCameraYawAngleDegrees);
//        differential_height_ = Constants.kCenterOfTargetHeight - Constants.kCameraZOffset;
    }

    public synchronized RigidTransform2d getFieldToVehicle(double timestamp) {
        return field_to_vehicle_.getInterpolated(new InterpolatingDouble(timestamp));
    }

    public synchronized Map.Entry<InterpolatingDouble, RigidTransform2d> getLatestFieldToVehicle() {
        return field_to_vehicle_.lastEntry();
    }

    public synchronized RigidTransform2d getPredictedFieldToVehicle(double lookahead_time) {
        return getLatestFieldToVehicle().getValue().transformBy(
                RigidTransform2d.fromVelocity(new RigidTransform2d.Delta(vehicle_velocity_.dx * lookahead_time,
                        vehicle_velocity_.dy * lookahead_time, vehicle_velocity_.dtheta * lookahead_time)));
    }

    public synchronized RigidTransform2d getFieldToCamera(double timestamp) {
        // NOTE our camera is correcting for offset, so we will lie about this
        return getFieldToVehicle(timestamp);
    }

    public synchronized List<RigidTransform2d> getCaptureTimeFieldToPeg() {
        List<RigidTransform2d> rv = new ArrayList<>();
        for (TrackReport report : peg_tracker.getTracks()) {
            rv.add(RigidTransform2d.fromTranslation(report.field_to_goal));
        }
        return rv;
    }

    public synchronized List<RigidTransform2d> getCaptureTimeFieldToLoadingStation() {
        List<RigidTransform2d> rv = new ArrayList<>();
        for (LoadingStationTracker.TrackReport report : loading_station_tracker.getTracks()) {
            rv.add(RigidTransform2d.fromTranslation(report.field_to_goal));
        }
        return rv;
    }

//    public synchronized List<ShooterAimingParameters> getAimingParameters(double current_timestamp,
//            Comparator<TrackReport> comparator) {
//        List<ShooterAimingParameters> rv = new ArrayList<>();
//        List<TrackReport> reports = goal_tracker_.getTracks();
//        Collections.sort(reports, comparator);
//
//        // turret fixed (latest) -> vehicle (latest) -> field
//        RigidTransform2d latest_turret_fixed_to_field = getPredictedFieldToVehicle(Constants.kAutoAimPredictionTime)
//                .transformBy(kVehicleToTurretFixed).inverse();
//
//        for (TrackReport report : reports) {
//            if (current_timestamp - report.latest_timestamp > kMaxTargetAge) {
//                continue;
//            }
//            // turret fixed (latest) -> vehicle (latest) -> field -> goals
//            RigidTransform2d latest_turret_fixed_to_goal = latest_turret_fixed_to_field
//                    .transformBy(RigidTransform2d.fromTranslation(report.field_to_goal));
//
//            // We can actually disregard the angular portion of this pose. It is
//            // the bearing that we care about!
//            rv.add(new ShooterAimingParameters(latest_turret_fixed_to_goal.getTranslation().norm(),
//                    new Rotation2d(latest_turret_fixed_to_goal.getTranslation().getX(),
//                            latest_turret_fixed_to_goal.getTranslation().getY(), true),
//                    report.id));
//        }
//        return rv;
//    }

    public synchronized void addFieldToVehicleObservation(double timestamp, RigidTransform2d observation) {
        field_to_vehicle_.put(new InterpolatingDouble(timestamp), observation);
    }

//    public synchronized void addTurretRotationObservation(double timestamp, Rotation2d observation) {
//        turret_rotation_.put(new InterpolatingDouble(timestamp), observation);
//    }

    public synchronized void addObservations(double timestamp, RigidTransform2d field_to_vehicle,
            RigidTransform2d.Delta velocity) {
        addFieldToVehicleObservation(timestamp, field_to_vehicle);
        vehicle_velocity_ = velocity;
    }

    public void addVisionUpdate(double timestamp, List<TargetInfo> vision_update) {
        List<Translation2d> field_to_pegs = new ArrayList<>();
        List<Translation2d> field_to_loading_station = new ArrayList<>();
        RigidTransform2d field_to_camera = getFieldToCamera(timestamp);
        if (!(vision_update == null || vision_update.isEmpty())) {
            for (TargetInfo target : vision_update) {
                
                Translation2d translation = new Translation2d(LightningMath.mm2ft(target.getLatidunalDistance()),
                        LightningMath.mm2ft(target.getLongitudinalDistance()));

                SmartDashboard.putString("Vision ", "type " + target.getType() + " - " +
                target.getLatidunalDistance() + "," + target.getLongitudinalDistance() + " :: " +
                        target.getTheta() + " :: " + target.getX() + "," + target.getY() + "," + target.getZ());
                if (target.getType() == 2) {
                    field_to_pegs.add(field_to_camera.transformBy(RigidTransform2d
                                    .fromTranslation(translation))
                            .getTranslation());
                } else {
                    field_to_loading_station.add(field_to_camera.transformBy(RigidTransform2d
                            .fromTranslation(translation))
                    .getTranslation());
                }
                
            }
        }
        synchronized (this) {
            peg_tracker.update(timestamp, field_to_pegs);
            loading_station_tracker.update(timestamp, field_to_loading_station);
        }
    }

    public synchronized void resetVision() {
        peg_tracker.reset();
        loading_station_tracker.reset();
    }

    public RigidTransform2d generateOdometryFromSensors(double left_encoder_delta_distance,
            double right_encoder_delta_distance, Rotation2d current_gyro_angle) {
        RigidTransform2d last_measurement = getLatestFieldToVehicle().getValue();
        return Kinematics.integrateForwardKinematics(last_measurement, left_encoder_delta_distance,
                right_encoder_delta_distance, current_gyro_angle);
    }

    public void outputToSmartDashboard() {
        RigidTransform2d odometry = getLatestFieldToVehicle().getValue();
        SmartDashboard.putNumber("robot_pose_x", odometry.getTranslation().getX());
        SmartDashboard.putNumber("robot_pose_y", odometry.getTranslation().getY());
        SmartDashboard.putNumber("robot_pose_theta", odometry.getRotation().getDegrees());
        List<RigidTransform2d> poses = getCaptureTimeFieldToPeg();
        for (RigidTransform2d pose : poses) {
            // Only output first goal
            SmartDashboard.putNumber("peg_pose_x", pose.getTranslation().getX());
            SmartDashboard.putNumber("peg_pose_y", pose.getTranslation().getY());
            break;
        }
        List<RigidTransform2d> ls_poses = getCaptureTimeFieldToLoadingStation();
        for (RigidTransform2d pose : ls_poses) {
            // Only output first goal
            SmartDashboard.putNumber("ls_pose_x", pose.getTranslation().getX());
            SmartDashboard.putNumber("ls_pose_y", pose.getTranslation().getY());
            break;
        }
    }
}
