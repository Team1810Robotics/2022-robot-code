package org.usd232.robotics.rapidreact.subsystems;

import org.usd232.robotics.rapidreact.Constants.HoodConstants;
import org.usd232.robotics.rapidreact.log.Logger;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// https://drive.google.com/file/d/1AjLDvokrLkQY14zsQigYv8ZqoQ9OmsL5/view?usp=sharing

public class VisionSubsystem extends SubsystemBase {
    /**
     * The logger.
     * 
     * @since 2018
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();

    private static double hoodDistance;
    private static double shooterSpeed;

    /** Turns the LimeLight On */
    public void limeLightOn() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(LLMode.ledForceOn.value);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(LLMode.camOff.value);
    }

    /** Turns the LimeLight Off */
    public void limeLightOff() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(LLMode.ledForceOff.value);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(LLMode.camOn.value);
        LOG.info("Turn Limelight Off");
    }

    /** @return if the limelight is on or off */
    public boolean getLimelight() {
        // return (NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getDouble(0) == 3)
        if (NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getDouble(0) == 3) {
            return true;
        } else {
            return false;
        }
    }

    /** @return whether the target is seen or not (0 or 1) */
    public double targetValid() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    }

    /** @return target's X offset from the limelight */
    public double targetXOffset() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    }

    /** @return target's Y offset from the limelight */
    public double targetYOffset() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    }

    /** @return target's area from the limelight */
    public double targetArea() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    }

    /**
     * gets the targeting value
     * hoodDistance[0] and shooterSpeed[1]
     * @return array contaning the calculated hoodDistance and the shooterSpeed
     */
    public double[] getTargetingValues() {
        final double ty = targetYOffset();

        if (targetValid() >= 1) {
            // Trying to use all threads we have (all 2 of them)
            new Thread(() -> {
                try {
                    if (ty <= -12) {
                        hoodDistance = -80.433 * Math.pow(ty, 2) - 2156.31 * ty - 14451.6;
    
                        if (hoodDistance > 0) {
                            // if the calculated hood distance is greater than 0 (AKA it's positive) set it to 0 because the hood cant go positive
                            hoodDistance = 0;
                        }
                        
                        if (hoodDistance < HoodConstants.FORWARD_HOOD_LIMIT) {
                            // if the calculated hood distance is past the max limit then set it to be at the max limit
                            hoodDistance = HoodConstants.FORWARD_HOOD_LIMIT;
                        } 
                    } else {
                        hoodDistance = 0;
                    }

                    // Jank
                    if (ty > 1.5) {
                        shooterSpeed = 0.435;
                    } else if (ty < 18.0 && ty > -4) {
                        shooterSpeed = 0.5;
                    } else if (ty < -4 && ty > -8.5) {
                        shooterSpeed = 0.6;
                    } else if (ty < -8.5 && ty > -12) {
                        shooterSpeed = 0.75;
                    } else if (ty <= -12) {
                        shooterSpeed = 1.0;
                    } else {
                        shooterSpeed = 0.5;
                        LOG.warn("Shooter Speed unmatched");
                    }

                } catch (Exception e) {
                    LOG.error(e);
                }
            }).run();
        }

        return new double[] {hoodDistance, shooterSpeed};
    }
    public enum LLMode {    // https://docs.limelightvision.io/en/latest/networktables_api.html

        // getEntry("ledMode")
        ledCurrentPipeline(0),
        ledForceOff(1),
        ledForceBlink(2),
        ledForceOn(3),

        // getEntry("camMode")
        camOn(1),
        camOff(0),

        // getEntry("pipeline")
        pipelineZero(0),
        pipelineOne(1),
        pipelineTwo(2),
        pipelineThree(3),
        pipelineFour(4),
        pipelineFive(5),
        pipelineSix(6),
        pipelineSeven(7),
        pipelineEight(8),
        pipelineNine(9),

        // getEntry("stream")
        streamStandard(0),
        streamPiPMain(1),
        streamPiPSecondary(2),

        // getEntry("snapshot")
        snapshotStop(0),
        snapshotStart(1);

        int value;

        LLMode(int value) {
            this.value = value;
        }
    }
}