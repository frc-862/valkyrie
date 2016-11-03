package org.usfirst.frc862.valkyrie.extensions;

import edu.wpi.first.wpilibj.Joystick;

public class Gamepad extends Joystick {

	public double joyValue;
	public Gamepad(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}

	public double getJoystickY(int side) {
		/**
		 * Gets the Y value for a joystick on the gamepad.
		 * Side:
		 *   0 - left joystick
		 *   1 - right joystick
		 */
		switch(side) {
			case 0:
				joyValue = getRawAxis(2)/2;
			case 1:
				joyValue = getRawAxis(5)/2;
			default:
				joyValue = 0;
		}
		if(Math.abs(joyValue) <= 0.1) {
			joyValue = 0;
		}
		return joyValue;
	}
}
