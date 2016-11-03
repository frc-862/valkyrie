package org.usfirst.frc862.valkyrie.extensions;

import edu.wpi.first.wpilibj.Joystick;

public class Gamepad extends Joystick {

	public Gamepad(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}

	public void getJoystickY(int side) {
		/**
		 * Gets the Y value for a joystick on the gamepad.
		 * Side:
		 *   1 - left joystick
		 *   2 - right joystick
		 */
		switch(side) {
			case 1:
				
			case 2:
				return getRawAxis(5);
		}
	}
}
