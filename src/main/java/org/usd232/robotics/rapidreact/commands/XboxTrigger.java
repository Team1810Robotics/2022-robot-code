package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.log.Logger;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/** Used to turn the Xbox LT & RT from analog to Digital */
public class XboxTrigger extends Trigger {
    /**
     * The logger.
     * 
     * @since 2018
     */
    private static final Logger LOG = new Logger();
    
    private XboxController xbox;
    private static double minValue = 0.1;
    private boolean right;

    public enum Hand {
        kLeft,
        kRight
    }

    public XboxTrigger(XboxController xbox, Hand hand) {
        this.xbox = xbox;

        switch (hand) {
            case kRight:
            this.right = true;

            case kLeft:
            this.right = false;

            default:
                LOG.error("INVALID HAND TYPE");
        }
    }

    @Override
    public boolean get() {
        if (xbox.getRightTriggerAxis() > minValue && right) {
            return true;
        }

        if (xbox.getLeftTriggerAxis() > minValue && !right) {
            return true;
        }

        return false;
    }
    
}
