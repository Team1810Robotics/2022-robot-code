package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.VisionConstants;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.NetworkTableInstance;

// https://drive.google.com/file/d/1AjLDvokrLkQY14zsQigYv8ZqoQ9OmsL5/view?usp=sharing


    // Turns long and bad words into short and nice words
public class VisionSubsystem {
    public static double targetValid = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    public static double targetXOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    public static double targetYOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    public static double targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
    public static double ledMode = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getDouble(0);

    private static double m_distance;

    /** Does some cool math to get the distance between the robot and the target */
    public static double getTargetDistance() {
        while (true) {
            targetYOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
            m_distance = (VisionConstants.TARGET_HEIGHT - VisionConstants.ROBOT_HEIGHT)
                    / Math.abs(Math.tan(VisionConstants.LIME_LIGHT_MOUNT_ANGLE + targetYOffset));
            return m_distance;
        }
    }

    /** Rotates the robot to target the goal using the limelight.
     *  Currently only targets on the X axis, since we don't have a hood yet.
     */
    public void limelightTarget(){

        // Turns the limelight LEDs on
        ledMode = 3;

        // Stops targeting if the limelight is off
        if(ledMode == 1){
            return;
        }

        // Checks if the crosshair is more or less than 1 degree away from the target
        if(targetXOffset < -1 || targetXOffset > 1){

            // Rotates the robot, with the speed proportional to how close it is to the target for more accuracy
            new ChassisSpeeds(0, 0, 0.5 * targetXOffset);
        }

        // If the crosshair is within 1 degree of the target, then the robot will stop moving to prevent jiggle.
        else if(targetXOffset > -1 || targetXOffset < 1){
            new ChassisSpeeds (0, 0, 0);
            
        }
    }
    public void stopTargeting(){

        // Turns off the LEDs and stops targeting
        ledMode = 1;
        return;

    }
}