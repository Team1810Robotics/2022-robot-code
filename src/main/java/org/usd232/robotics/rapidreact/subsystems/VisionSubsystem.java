package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.VisionConstants;

import edu.wpi.first.networktables.NetworkTableInstance;

// https://drive.google.com/file/d/1AjLDvokrLkQY14zsQigYv8ZqoQ9OmsL5/view?usp=sharing

public class VisionSubsystem {
    public static double targetValid = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    public static double targetXOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    public static double targetYOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    public static double targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

    private static double m_distance;

    public static double getTargetDistance() {
        while (true) {
            targetYOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
            m_distance = (VisionConstants.TARGET_HEIGHT - VisionConstants.ROBOT_HEIGHT)
                    / Math.abs(Math.tan(VisionConstants.LIME_LIGHT_MOUNT_ANGLE + targetYOffset));
            return m_distance;
        }
    }

}
