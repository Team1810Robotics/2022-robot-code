package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.HoodConstants;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay; // Motor controller is a spike
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HoodSubsystem extends SubsystemBase {
    
    private static final Relay hood = new Relay(HoodConstants.MOTOR_PORT, Relay.Direction.kBoth);
    public static final Encoder hoodEncoder = new Encoder(HoodConstants.HOOD_ENCODER_CHANNEL[1], HoodConstants.HOOD_ENCODER_CHANNEL[0]);
    public static final DigitalInput hoodLS = new DigitalInput(HoodConstants.HOOD_LIMIT_SWITCH_CHANNEL);

    private boolean forward;

    /** Makes the hood move forward */
    public void forwardHood() {
        if (hoodEncoder.getDistance() <= HoodConstants.FORWARD_HOOD_LIMIT) {
            hood.set(Relay.Value.kReverse);
        }
    }

    /** Makes the hood stop moving*/
    public void stopHood() {
        hood.set(Relay.Value.kOff);
    }

    /** Makes the hood move backward */
    public void reverseHood() {
        if (!hoodLS.get()) {
            hood.set(Relay.Value.kForward);
        }
    }

    /** Sets the hood back to its default position */
    public void resetHood() {
        if (!hoodLS.get()) {
            while (!hoodLS.get()) {
                hood.set(Relay.Value.kReverse);
            }
            hood.set(Relay.Value.kOff);
            hoodEncoder.reset();
        }
    }
    
    public void zeroEncoder() {
        hoodEncoder.reset();
    }
    
    public void setHood(double target) { // TODO: Test
        
        if (target > hoodEncoder.getDistance()) {
            forward = true;

        } else if (target < hoodEncoder.getDistance()) {
            forward = false;

        } else if ((target - HoodConstants.HOOD_DEADBAND) >= HoodSubsystem.hoodEncoder.getDistance() 
                    || (target + HoodConstants.HOOD_DEADBAND) <= HoodSubsystem.hoodEncoder.getDistance()) { // If at + or - DEADBAND then dont move 
            return;
        }

        if (forward) {
            this.forwardHood();
        } else {
            this.reverseHood();
        }
    }

}