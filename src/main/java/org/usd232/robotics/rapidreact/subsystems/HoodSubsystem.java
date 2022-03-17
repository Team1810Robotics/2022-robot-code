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
     * @since 2018
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();
    
    private static final Relay hood = new Relay(HoodConstants.MOTOR_PORT, Relay.Direction.kBoth);
    public static final Encoder hoodEncoder = new Encoder(HoodConstants.HOOD_ENCODER_CHANNEL[1], HoodConstants.HOOD_ENCODER_CHANNEL[0]);
    public static final DigitalInput hoodLS = new DigitalInput(HoodConstants.HOOD_LIMIT_SWITCH_CHANNEL);

    private static final VisionSubsystem visionSubsystem = new VisionSubsystem();

    private boolean forward;

    /** Makes the hood move forward */
    public void forwardHood() {
        if (hoodEncoder.getDistance() >= HoodConstants.FORWARD_HOOD_LIMIT) {
            hood.set(Relay.Value.kReverse);
        } else {
            hood.set(Relay.Value.kOff);
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
        } else {
            hood.set(Relay.Value.kOff);
        }
    }

    /** Sets the hood back to its default position */
    public void resetHood() {
        if (!hoodLS.get()) {
            while (!hoodLS.get()) {
                LOG.info("in hood hile loop");
                this.reverseHood();
            }
            hood.set(Relay.Value.kOff);
            hoodEncoder.reset();
        }
    }
    
    public void zeroEncoder() {
        hoodEncoder.reset();
    }
    
    public void setHood(double target) {

        double distance = hoodEncoder.getDistance();
        
        // The signs are right: https://drive.google.com/file/d/10tB8zsp_gBse0LokIxdII_zqo9CH4YSJ/view?usp=sharing
        if ((distance - HoodConstants.HOOD_DEADBAND) <= target 
                    && (distance + HoodConstants.HOOD_DEADBAND) >= target) { // If at + or - DEADBAND then dont move
            this.stopHood();
            return;

        } else {
            if (target < distance) {
                forward = true;
                LOG.info("forward = true;");
                
            } else if (target > distance) {
                forward = false;
                LOG.info("forward = false;");
            } else {
                LOG.info("you fileailed");
            }
        }

        if (visionSubsystem.getLimelight()) {
            if (forward) {
                this.forwardHood();
            } else {
                this.reverseHood();
            }
        } else {
            this.stopHood();
            LOG.info("Stop Hood");
        }
    }

}