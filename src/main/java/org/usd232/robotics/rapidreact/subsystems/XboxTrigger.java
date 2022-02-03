package org.usd232.robotics.rapidreact.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/** Used to turn the Xbox LT & RT from analog to Digital */
public class XboxTrigger extends Trigger {
    private XboxController m_xbox;
    private static double minValue = 0.1;

    public XboxTrigger(XboxController xbox) {
        this.m_xbox = xbox;
    }

    @Override
    public boolean get() {
        if (m_xbox.getLeftTriggerAxis() > minValue || m_xbox.getRightTriggerAxis() > minValue) {
            return true;
        }
        return false;
    }
    
}
