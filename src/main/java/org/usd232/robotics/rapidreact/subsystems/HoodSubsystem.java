package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.HoodConstants;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay; // Motor controller is a spike
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HoodSubsystem extends SubsystemBase {
    
    private static final Relay hood = new Relay(HoodConstants.MOTOR_PORT, Relay.Direction.kBoth);
    public static final Encoder hoodEncoder = new Encoder(HoodConstants.HOOD_ENCODER_CHANNEL[0], HoodConstants.HOOD_ENCODER_CHANNEL[1]);
    public static final DigitalInput hoodLS = new DigitalInput(HoodConstants.HOOD_LIMIT_SWITCH_CHANNEL);

    public void forwardHood() {
        if (hoodEncoder.getDistance() >= HoodConstants.FORWARD_HOOD_LIMIT) {
            hood.set(Relay.Value.kOff);

        } else {
            hood.set(Relay.Value.kForward);
        }

    }

    public void stopHood() {
        hood.set(Relay.Value.kOff);
    }

    public void reverseHood() {
        if (hoodLS.get() == true) {
            hood.set(Relay.Value.kOff);
            hoodEncoder.reset();
        }  else {
            hood.set(Relay.Value.kReverse);
        }

    }

    public void resetHood() {
        if (!hoodLS.get()) {
            while (!hoodLS.get()) {
                hood.set(Relay.Value.kReverse);
            }
            hood.set(Relay.Value.kOff);
            hoodEncoder.reset();
        }
    }

}
