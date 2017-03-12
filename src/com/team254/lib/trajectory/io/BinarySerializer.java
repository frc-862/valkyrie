package com.team254.lib.trajectory.io;

import java.io.DataOutputStream;
import java.io.IOException;

import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.Trajectory.Segment;

/**
 * Serializes a Path to a binary file.
 * 
 * @author mstojcevich
 */
public class BinarySerializer {

	public void serialize(DataOutputStream outStream, Path path) throws IOException {
		// Write the title string, followed by a null terminator
		outStream.write(path.getName().getBytes());
		outStream.writeByte(0); // End of string

		// Write all of the segments
		path.goLeft();
		outStream.writeInt(path.getLeftWheelTrajectory().getNumSegments());
		serializeTrajectory(outStream, path.getLeftWheelTrajectory());
		serializeTrajectory(outStream, path.getRightWheelTrajectory());
	}

	private void serializeTrajectory(DataOutputStream outStream, Trajectory trajectory) throws IOException {
		for (int i = 0; i < trajectory.getNumSegments(); ++i) {
			Segment segment = trajectory.getSegment(i);

			outStream.writeFloat((float) segment.pos);
			outStream.writeFloat((float) segment.vel);
			outStream.writeFloat((float) segment.acc);
			outStream.writeFloat((float) segment.jerk);
			outStream.writeFloat((float) segment.heading);
			outStream.writeFloat((float) segment.dt);
			outStream.writeFloat((float) segment.x);
			outStream.writeFloat((float) segment.y);
		}
	}

}
