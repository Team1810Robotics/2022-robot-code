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

    // TODO: Simplify to only 2 NetworkTable grabs rather than 4
    public static boolean llOn = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
    public static boolean llOff = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    public static boolean visOn = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
    public static boolean visOff = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);

    private static double m_distance;

    /** For ShuffleBoard */
    public static boolean OnOffLL;

    /** Does some cool math to get the distance between the robot and the target */
    public static double getTargetDistance() {
        while (true) {
            targetYOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
            m_distance = (VisionConstants.TARGET_HEIGHT - VisionConstants.ROBOT_HEIGHT)
                    / Math.abs(Math.tan(VisionConstants.LIME_LIGHT_MOUNT_ANGLE + targetYOffset));
            return m_distance;
        }
    }

    /** Turns the LimeLight On */
    public static void limeLightOn() {
        llOn = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);
        visOn = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
        OnOffLL = true;
    }

    /** Turns the LimeLight Off */
    public static void limeLightOff() {
        llOff = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        visOff = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
        OnOffLL = false;
    }
}