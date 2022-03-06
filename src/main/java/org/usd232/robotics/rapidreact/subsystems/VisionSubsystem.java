package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.VisionConstants;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// https://drive.google.com/file/d/1AjLDvokrLkQY14zsQigYv8ZqoQ9OmsL5/view?usp=sharing

public class VisionSubsystem extends SubsystemBase {
    // Turns long and bad words into short and nice words
    public static double targetValid = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    public static double targetXOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    public static double targetYOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    public static double targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

    public boolean ledMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    public boolean camMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);

    private static double m_distance;

    /** For ShuffleBoard */
    public static boolean OnOffLL;

    /** Uses the tangent to find the distance from the target plane */
    public static double getTargetDistance() {
        // Get most recent angle
        double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);

            m_distance = (VisionConstants.TARGET_HEIGHT - VisionConstants.ROBOT_HEIGHT)
                    / Math.abs(Math.tan(VisionConstants.LIME_LIGHT_MOUNT_ANGLE + ty));
            return m_distance;
    }
    
    /** @return the distance of the Limelight to the center of the target */
    public static double getTargetDistanceOffset() {
        return (getTargetDistance() + 0.678);
    }

    /** Turns the LimeLight On */
    public void limeLightOn() {
        ledMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(LLMode.ledOn.value);
        camMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(LLMode.camOff.value);
        OnOffLL = true;
    }

    /** Turns the LimeLight Off */
    public void limeLightOff() {
        ledMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(LLMode.ledOff.value);
        camMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(LLMode.camOn.value);
        OnOffLL = false;
    }

    public enum LLMode {
        ledOn(3),
        ledOff(1),
        camOn(1),
        camOff(0);

        int value;

        LLMode(int value) {
            this.value = value;
        }
    }
}