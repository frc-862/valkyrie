package org.usfirst.frc862.valkyrie.commands;
import org.usfirst.frc862.valkyrie.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class PlaybackRecordedMotion extends Command {

//    private String m_path_name;
//    private RecordedPath path;
//    private DriveTrain drive;
 
    public PlaybackRecordedMotion(String path_name) {
//        m_path_name = path_name;
        requires(Robot.driveTrain);
        requires(Robot.shifter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        Robot.shifter.upShift();
//        drive = Robot.driveTrain;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
