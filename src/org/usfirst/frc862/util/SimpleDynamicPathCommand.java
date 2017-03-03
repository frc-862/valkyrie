package org.usfirst.frc862.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.usfirst.frc862.valkyrie.Robot;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain;
import org.usfirst.frc862.valkyrie.subsystems.DriveTrain.Modes;

import com.team254.lib.trajectory.FollowerInterface;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.TrajectoryFollower;
import com.team254.lib.trajectory.TrajectoryGenerator;
import com.team254.lib.trajectory.WaypointSequence;
import com.team254.lib.trajectory.io.TextFileDeserializer;
import com.team254.lib.trajectory.io.TextFileSerializer;
import com.team254.lib.util.ChezyMath;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;

public class SimpleDynamicPathCommand extends Command {
    private WaypointSequence points = new WaypointSequence(10);
    private TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    private FollowerInterface followerLeft = new TrajectoryFollower();
    private FollowerInterface followerRight = new TrajectoryFollower();
    private Notifier notifier;
    private Path path;
    public double pathP = 114.65;//FPS*60/pi*Wheel Diameter 
    public double pathI = 0;
    public double pathD = 0;
    public double pathV = 76.43;//FPS^2*60/pi*Wheel Diameter
    public double pathA = 0;
    public double wheelBase;//insert size of robot wheel size base
    public double pathTurn;
    public SimpleDynamicPathCommand(String name) {
        super(name);
        
        config.dt = 0.02;//add prefered time slice delta
        config.max_acc = 8 / 3;//add path max acceleration
        config.max_jerk = 25;//add path max jerk
        config.max_vel = 3.5;//add path max velocity
        
        requires(Robot.driveTrain);

        notifier = new Notifier(()-> followPath());
    }

    public void addWaypoint(double x, double y, double theta) {
        points.addWaypoint(new WaypointSequence.Waypoint(x, y, Math.toRadians(theta)));
    }

    public TrajectoryGenerator.Config getConfig() {
        return config;
    }
    
    protected boolean loadPath() {
        try {
            File file = new File(getFileName());

            if (!file.exists()) {
                
                return false;
            }

            String contents = new String(Files.readAllBytes(Paths.get(getFileName())));            
            TextFileDeserializer deserializer = new TextFileDeserializer();
            path = deserializer.deserialize(contents);

            
               
            
        } catch (IOException e) {
            
            return false;
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
          
        } else {
          
        }    
    }

    protected String getFileName() {
        return "/home/lvuser/" + getName() + ".txt";
    }
    
    private static boolean writeFile(String path, String data) {
        try {
            
            File file = new File(path);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        } catch (IOException e) {
            
            return false;
        }

        return true;
    }

    protected void generatePath() {
        
        path = PathGenerator.makePath(points, config, wheelBase, getName());
    }
    
    @Override
    protected void initialize() { 
        //If you do not use Talons do not use this code
        Robot.driveTrain.setMode(Modes.OPEN_LOOP);
        
        if (!loadPath()) {
           
            generatePath();
            savePath();
        }
//add encoder reset here
        
        Robot.driveTrain.resetDistance();        
        followerLeft.configure(pathP, pathI, pathD, pathV, pathA);        
        followerRight.configure(pathP, pathI, pathD, pathV, pathA);

        followerLeft.setTrajectory(path.getLeftWheelTrajectory());
        followerLeft.reset();
        followerRight.setTrajectory(path.getRightWheelTrajectory());
        followerRight.reset();

        notifier.startPeriodic(config.dt);
    }

    private void followPath() {
        DriveTrain drive = Robot.driveTrain;
        double distanceL = drive.getLeftDistance();
        //get left encoder 
        double distanceR = drive.getRightDistance();
        //get right encoder
        double leftPower = followerLeft.calculate(distanceL);
        double rightPower = followerRight.calculate(distanceR);
        //comment out if there is no gyroscope on the robot
        double goalHeading = followerLeft.getHeading();
        double observedHeading = drive.getGyroAngleInRadians();

        double angleDiff = ChezyMath.getDifferenceInAngleDegrees(observedHeading, Math.toDegrees(goalHeading));

        //remove turn if no gyro
        double turn = pathTurn * angleDiff;
        double requestedLeft = leftPower + turn;
        double requestedRight = rightPower - turn;
        //set motors here
        drive.set(requestedLeft, requestedRight);

        
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected void end() {
        notifier.stop();
        
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
