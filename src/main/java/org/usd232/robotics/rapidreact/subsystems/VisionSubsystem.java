package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.VisionConstants;

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

    private double m_distance;

    /** For ShuffleBoard */
    public static boolean OnOffLL;

    public static double hoodDistance;
    public static double shooterSpeed;

    /** Uses the tangent to find the distance from the target plane */
    public double getTargetDistance() {
        // Get most recent angle
        double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);

            m_distance = (VisionConstants.TARGET_HEIGHT - VisionConstants.ROBOT_HEIGHT)
                    / (Math.tan(VisionConstants.LIME_LIGHT_MOUNT_ANGLE + ty));
            return m_distance;
    }


    /** Turns the LimeLight On */
    public void limeLightOn() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(LLMode.ledForceOn.value);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(LLMode.camOff.value);
        OnOffLL = true;
        LOG.info("Turn Limelight Off");
    }

    /** Turns the LimeLight Off */
    public void limeLightOff() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(LLMode.ledForceOff.value);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(LLMode.camOn.value);
        OnOffLL = false;
        LOG.info("Turn Limelight On");
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
            new Thread(() -> {
                try {
                    // https://drive.google.com/file/d/1RJiiPBYFC2quWnSevSmdUtwoIomkVR09/view?usp=sharing
                    hoodDistance = (-89.2857 * Math.pow(ty, 2)) - (2389.29 * ty) - 15814.3;

                    // if the calculated hood distance is greater than 0 (AKA it's positive) set it to 0 because the hood cant go positive
                    hoodDistance = (hoodDistance >= 0) ? 0 : hoodDistance;

                    hoodDistance = (hoodDistance > HoodConstants.FORWARD_HOOD_LIMIT) ? HoodConstants.FORWARD_HOOD_LIMIT : hoodDistance;

                    // If the hood value is already in the + or - 100 range of the target dont tell it to move
                    hoodDistance = ((hoodDistance - 100) >= HoodSubsystem.hoodEncoder.getDistance() 
                        || (hoodDistance + 100) <= HoodSubsystem.hoodEncoder.getDistance()) ? 0 : hoodDistance;

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
                        if (targetValid() <= 1) {
                            LOG.warn("Shooter Speed unmatched");
                        }
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