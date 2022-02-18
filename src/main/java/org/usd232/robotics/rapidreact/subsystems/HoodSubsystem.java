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

    /** Makes the hood move forward */
    public void forwardHood() {
        if (hoodEncoder.getDistance() >= HoodConstants.FORWARD_HOOD_LIMIT) {
            hood.set(Relay.Value.kOff);

        } else {
            hood.set(Relay.Value.kForward);
        }

    }

    /** Sets the angle of the hood to a specific value. This is probably REALLY jank.
    * @param targetValue   The value you want the hood to go to 
    */
    public void setHood(double targetValue) {

        // If the hood is lower than it should be, move it forward
        if(hoodEncoder.get() < targetValue){
            forwardHood();
        }

        // If it's higher, move it backward
        else if (hoodEncoder.get() > targetValue){
            reverseHood();
        }

        // When the hood is at to the target value, stop moving. This is the jank part.
        if(hoodEncoder.get() == targetValue) {
           stopHood();
           return;
        }

    }

    /** Makes the hood stop moving*/
    public void stopHood() {
        hood.set(Relay.Value.kOff);
    }

    /** Makes the hood move backward */
    public void reverseHood() {
        if (hoodLS.get() == true) {
            hood.set(Relay.Value.kOff);
            hoodEncoder.reset();
        }  else {
            hood.set(Relay.Value.kReverse);
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

}
