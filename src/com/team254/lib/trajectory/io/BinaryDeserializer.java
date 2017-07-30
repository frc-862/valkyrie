package com.team254.lib.trajectory.io;

import com.team254.lib.trajectory.Path;

import java.io.DataInputStream;
import java.io.IOException;
import com.team254.lib.trajectory.Trajectory;

/**
 * Deserializes a binary file to a Path
 * 
 * @author mstojcevich
 */
public class BinaryDeserializer {

	public Path deserialize(DataInputStream serialized) throws IOException {
		// Read in the name (keep reading in chars until we reach a null-terminator
		StringBuilder nameBuilder = new StringBuilder(32);
		for (char c = (char) serialized.readUnsignedByte(); c != 0; c = (char) serialized.readUnsignedByte()) {
			nameBuilder.append(c);
		}
		String name = nameBuilder.toString();

		// Read in the number of elements in the path
		int numElements = serialized.readInt();

		// Read in the left path
		Trajectory left = new Trajectory(numElements);
		for (int i = 0; i < numElements; ++i) {
			left.setSegment(i, loadSegment(serialized));
		}

		// Read in the right path
		Trajectory right = new Trajectory(numElements);
		for (int i = 0; i < numElements; ++i) {
			right.setSegment(i, loadSegment(serialized));
		}

		return new Path(name, new Trajectory.Pair(left, right));
	}

	private Trajectory.Segment loadSegment(DataInputStream serialized) throws IOException {
		Trajectory.Segment segment = new Trajectory.Segment();

		segment.pos = serialized.readDouble();
		segment.vel = serialized.readDouble();
		segment.acc = serialized.readDouble();
		segment.jerk = serialized.readDouble();
		segment.heading = serialized.readDouble();
		segment.dt = serialized.readDouble();
		segment.x = serialized.readDouble();
		segment.y = serialized.readDouble();

		return segment;
	}

}
