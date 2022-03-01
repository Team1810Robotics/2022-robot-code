package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.HoodConstants;

import org.usd232.robotics.rapidreact.log.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay; // Motor controller is a spike
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HoodSubsystem extends SubsystemBase {
    /**
     * The logger.
     * 
     * @since 2018/
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();
    
    private static final Relay hood = new Relay(HoodConstants.MOTOR_PORT, Relay.Direction.kBoth);
    public static final Encoder hoodEncoder = new Encoder(HoodConstants.HOOD_ENCODER_CHANNEL[1], HoodConstants.HOOD_ENCODER_CHANNEL[0]);
    public static final DigitalInput hoodLS = new DigitalInput(HoodConstants.HOOD_LIMIT_SWITCH_CHANNEL); 

    private HoodSubsystem() {}

    /** Makes the hood move forward */
    public static void forwardHood() {
        LOG.info("Hood of forward");
        if (hoodEncoder.getDistance() >= HoodConstants.FORWARD_HOOD_LIMIT) {
            hood.set(Relay.Value.kOff);

        } else {
        }
        hood.set(Relay.Value.kReverse);
    }

    /** Makes the hood stop moving*/
    public static void stopHood() {
        hood.set(Relay.Value.kOff);
        LOG.info("Hood of stop");
    }

    /** Makes the hood move backward */
    public static void reverseHood() {
        LOG.info("Hood of reverse");
        if (hoodLS.get() == true) {
            hood.set(Relay.Value.kOff);
            hoodEncoder.reset();
        } else {
            hood.set(Relay.Value.kForward);
        }

    }

    /** Sets the hood back to its default position */
    public static void resetHood() {
        if (!hoodLS.get()) {
            while (!hoodLS.get()) {
                hood.set(Relay.Value.kReverse);
            }
            hood.set(Relay.Value.kOff);
            hoodEncoder.reset();
        }
    }

    public static void zeroEncoder() {
        hoodEncoder.reset();
    }

}
