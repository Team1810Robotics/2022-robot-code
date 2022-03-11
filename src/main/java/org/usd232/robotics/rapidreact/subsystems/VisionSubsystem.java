package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.VisionConstants;

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

    /** Uses the tangent to find the distance from the target plane */
    public double getTargetDistance() {
        // Get most recent angle
        double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);

            m_distance = (VisionConstants.TARGET_HEIGHT - VisionConstants.ROBOT_HEIGHT)
                    / Math.abs(Math.tan(VisionConstants.LIME_LIGHT_MOUNT_ANGLE + ty));
            return m_distance;
    }
    
    /** @return the distance of the Limelight to the center of the target */
    public double getTargetDistanceOffset() {
        return (this.getTargetDistance() + 0.678);
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

    public static double getHoodDistance() {
        new Thread(() -> {
            try {
                // hoodDistance = formula here;

            } catch (Exception e) {
                LOG.error(e);
            }
        }).run();

        return hoodDistance;
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