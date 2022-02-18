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

    public static double ledMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getDouble(0);
    public static double camMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").getDouble(0);

    private static double m_distance;

    /** For ShuffleBoard */
    public static boolean OnOffLL;

    /** Uses the tangent to find the distance from the target plane */
    public static double getTargetDistance() {
            targetYOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
            m_distance = (VisionConstants.TARGET_HEIGHT - VisionConstants.ROBOT_HEIGHT)
                    / Math.abs(Math.tan(VisionConstants.LIME_LIGHT_MOUNT_ANGLE + targetYOffset));
            return m_distance;
    }
    
    /** Returns the distance of the Limelight to the center of the target */
    public static double getTargetDistanceOffset() {
        return (getTargetDistance() + 0.678);
    }

    /** Turns the LimeLight On */
    public static void limeLightOn() {
        ledMode = 3;
        camMode = 0;
        OnOffLL = true;
    }

    /** Turns the LimeLight Off */
    public static void limeLightOff() {
        ledMode = 1;
        camMode = 1;
        OnOffLL = false;
    }
}