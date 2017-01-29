package edu.wpi.first.wpilibj.buttons;

import edu.wpi.first.wpilibj.GenericHID;
import org.usfirst.frc862.util.Logger;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger.ButtonScheduler;
import edu.wpi.first.wpilibj.command.Command;

public class LightningButton extends JoystickButton {

    public LightningButton(GenericHID joystick, int buttonNumber) {
        super(joystick, buttonNumber);
        // TODO Auto-generated constructor stub
    }

    public void whileActive(final Command command) {
        new ButtonScheduler() {

            private boolean active = false;
            private boolean m_pressedLast = false;
            private boolean m_pressedLast2 = false;
            private boolean m_pressedLast3 = false;

            @Override
            public void execute() {
                boolean pressed = get();
                
                Logger.debug("Pressed: %s %s %s %s",
                        ((pressed) ? "0" : "1"), 
                        ((m_pressedLast) ? "0" : "1"), 
                        ((m_pressedLast2) ? "0" : "1"), 
                        ((m_pressedLast3) ? "0" : "1")
                        );
                if (pressed || m_pressedLast || m_pressedLast2 || m_pressedLast3) {
                    if (!active) {
                        Logger.debug("Start");
                        command.start();
                        active = true;
                    }
                } else {
                    if (active) {
                        Logger.debug("Cancel");
                        command.cancel();
                        active = false;
                    }
                }
                m_pressedLast3 = m_pressedLast2;
                m_pressedLast2 = m_pressedLast;
                m_pressedLast = pressed;
            }
        }.start();
    }

}
