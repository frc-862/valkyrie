package org.usfirst.frc862.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.usfirst.frc862.valkyrie.Constants;
import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain.Modes;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.trajectory.TrajectoryGenerator;
import com.team254.lib.trajectory.WaypointSequence;
import com.team254.lib.trajectory.io.TextFileDeserializer;
import com.team254.lib.trajectory.io.TextFileSerializer;
import com.team254.lib.util.ChezyMath;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DynamicPathCommand extends Command {
    private WaypointSequence points = new WaypointSequence(10);
    private TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    private CommandLogger logger;
    private TrajectoryFollower followerLeft = new TrajectoryFollower();
    private TrajectoryFollower followerRight = new TrajectoryFollower();
    private Notifier notifier;
    private Path path;
    private double starting_heading;
    protected boolean testing = false;

    public DynamicPathCommand(String name) {
        super(name);
        
        config.dt = Constants.Path_dt;
        config.max_acc = Constants.Path_max_acc;
        config.max_jerk = Constants.Path_max_jerk;
        config.max_vel = Constants.Path_max_vel;
        
        requires(Robot.driveTrain);

        logger = new CommandLogger(this.getName());
        logger.addDataElement("projected_left_pos");
        logger.addDataElement("requested_left_vel");
        logger.addDataElement("actual_left_pos");
        logger.addDataElement("projected_left_vel");
        logger.addDataElement("actual_left_vel");
        logger.addDataElement("projected_right_pos");
        logger.addDataElement("requested_right_pos");
        logger.addDataElement("actual_right_pos");
        logger.addDataElement("projected_right_vel");
        logger.addDataElement("actual_right_vel");
        logger.addDataElement("projected_heading");
        logger.addDataElement("actual_heading");
        logger.addDataElement("angle_diff");
        
        notifier = new Notifier(()-> followPath());
    }

    public void addWaypoint(double x, double y, double theta) {
        points.addWaypoint(new WaypointSequence.Waypoint(x, y, Math.toRadians(theta)));
    }

    public TrajectoryGenerator.Config getConfig() {
        return config;
    }
    
    protected boolean loadPath() {
        if (testing ) {
            return false;
        } else {
            try {
                File file = new File(getFileName());

                if (!file.exists()) {
                    Logger.debug("Unable to load file " + getFileName());
                    return false;
                }

                String contents = new String(Files.readAllBytes(Paths.get(getFileName())));
                TextFileDeserializer deserializer = new TextFileDeserializer();
                path = deserializer.deserialize(contents);

                Logger.debug("Loaded file " + getFileName() + " with " + path.getLeftWheelTrajectory().getNumSegments()
                        + " points");

            } catch (IOException e) {
                Logger.error(e.toString());
                return false;
            }
        }
        return true;
    }
    
    protected void savePath() {
        // Outputs to the directory supplied as the first argument.
        TextFileSerializer js = new TextFileSerializer();
        String serialized = js.serialize(path);
        //System.out.print(serialized);
        String fullpath = getFileName();
        if (!writeFile(fullpath, serialized)) {
          Logger.debug(fullpath + " could not be written!!!!1");
        } else {
          Logger.debug("Wrote " + fullpath);
        }    
    }

    protected String getFileName() {
        return "/home/lvuser/" + getName() + ".txt";
    }
    
    private static boolean writeFile(String path, String data) {
        try {
            Logger.debug("Writing path file to: " + path);
            File file = new File(path);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        } catch (IOException e) {
            Logger.error(e.toString());
            return false;
        }

        return true;
    }

    protected void generatePath() {
        path = PathGenerator.makePath(points, config, Constants.wheelBase, getName());
    }
    
    @Override
    protected void initialize() {
        Logger.debug("DynamicPathCommand init " + Timer.getFPGATimestamp());
        
        Robot.driveTrain.setMode(Modes.VELOCITY);
        Robot.shifter.downShift();
        
        if (!loadPath()) {
            Logger.debug("Generating path");
            generatePath();
            savePath();
        }

        Robot.driveTrain.resetDistance();
        starting_heading = Robot.driveTrain.getGyroAngle();
        followerLeft.configure(Constants.pathP, Constants.pathI, Constants.pathD, Constants.pathV, Constants.pathA);        
        followerRight.configure(Constants.pathP, Constants.pathI, Constants.pathD, Constants.pathV, Constants.pathA);

        followerLeft.setTrajectory(path.getLeftWheelTrajectory());
        followerLeft.reset();
        followerRight.setTrajectory(path.getRightWheelTrajectory());
        followerRight.reset();

        notifier.startPeriodic(config.dt);
    }

    private void followPath() {
        DriveTrain drive = Robot.driveTrain;
        double distanceL = drive.getLeftDistance();
        double distanceR = drive.getRightDistance();

        Trajectory.Segment left = followerLeft.getSegment();
        Trajectory.Segment right = followerRight.getSegment();
        
        double speedLeft = followerLeft.calculate(distanceL);
        double speedRight = followerRight.calculate(distanceR);
        
        double goalHeading = Math.toDegrees(followerLeft.getHeading());
        double observedHeading = ChezyMath.getDifferenceInAngleDegrees(drive.getGyroAngle(), starting_heading);
        double angleDiff = ChezyMath.getDifferenceInAngleDegrees(observedHeading, goalHeading);

        double turn = Constants.pathTurn * angleDiff;
        double requestedLeft = speedLeft + turn;
        double requestedRight = speedRight - turn;
        
        drive.set(requestedLeft, requestedRight);

        logger.set("projected_left_pos", left.pos);
        logger.set("requested_left_vel", LightningMath.rpm2fps(requestedLeft));
        logger.set("actual_left_pos", distanceL);
        logger.set("projected_left_vel", left.vel);
        logger.set("actual_left_vel", drive.getLeftVelocity());
        logger.set("projected_right_pos", right.pos);
        logger.set("requested_right_vel", LightningMath.rpm2fps(requestedRight));
        logger.set("actual_right_pos", distanceR);
        logger.set("projected_right_vel", right.vel);
        logger.set("actual_right_vel", drive.getRightVelocity());
        logger.set("projected_heading", ChezyMath.boundAngleNeg180to180Degrees(goalHeading));
        logger.set("actual_heading", observedHeading);
        logger.set("angle_diff", angleDiff);
        logger.write();
    }

    @Override
    protected void execute() {
        logger.drain();
    }

    @Override
    protected void end() {
        notifier.stop();
        logger.drain();
        logger.flush();
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected boolean isFinished() {
        return followerLeft.isFinishedTrajectory() && 
                followerRight.isFinishedTrajectory();
    }

}
