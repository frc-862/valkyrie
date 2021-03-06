/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2016. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc862.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 *
 * @author bradmiller
 */
public class POVButton extends Button {

  GenericHID joystick;
  int position;

  /**
   * Create a joystick button for triggering commands
   *$
   * @param joystick The GenericHID object that has the button (e.g. Joystick,
   *        KinectStick, etc)
   * @param buttonNumber The button number (see
   *        {@link GenericHID#getRawButton(int) }
   */
  public POVButton(GenericHID joystick, int position) {
    this.joystick = joystick;
    this.position = position;
  }

  /**
   * Gets the value of the joystick button
   *$
   * @return The value of the joystick button
   */
  public boolean get() {
    return joystick.getPOV() == position;
  }
}
