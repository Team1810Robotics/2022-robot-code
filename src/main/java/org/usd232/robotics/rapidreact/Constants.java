package org.usd232.robotics.rapidreact;

import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final class DriveConstants {
        /**
         * The left-to-right distance between the drivetrain wheels
         *
         * Should be measured from center to center.
         */
        public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.5;
        /**
         * The front-to-back distance between the drivetrain wheels.
         *
         * Should be measured from center to center.
        */
        public static final double DRIVETRAIN_WHEELBASE_METERS = 0.64928;

        public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 1;
        public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 2;
        public static final int FRONT_LEFT_MODULE_STEER_CANCODER = 10;
        public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(329.15);
        
        public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 3;
        public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 4;
        public static final int FRONT_RIGHT_MODULE_STEER_CANCODER = 9;
        public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(9.76);
        
        public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 7;
        public static final int BACK_LEFT_MODULE_STEER_MOTOR = 8;
        public static final int BACK_LEFT_MODULE_STEER_CANCODER = 12;
        public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(221.57);
        
        public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 5;
        public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 6;
        public static final int BACK_RIGHT_MODULE_STEER_CANCODER = 11;
        public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(91.58);
    }
    
    public static final class ModuleConstants {
        /**
         * The maximum velocity of the robot in meters per second.
         * <p>
         * This is a measure of how fast the robot should be able to drive in a straight line.
         */
        // Freespeed: 4.96824 m/s^2
        // Calculated: 4.4231268 m/s^2
        // Real: TODO m/^2 (after whole robot is assembled)
        public static final double MAX_VELOCITY_METERS_PER_SECOND = 5680.0 / 60.0 *
        SdsModuleConfigurations.MK4_L2.getDriveReduction() *
        SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI;

        /**
         * The maximum voltage that will be delivered to the drive motors.
         * <p>
         * This can be reduced to cap the robot's maximum speed. Typically, this is useful during initial testing of the robot.
         */
        public static final double MAX_VOLTAGE = 6.0;

        /**
         * The maximum angular velocity of the robot in radians per second.
         * <p>
         * This is a measure of how fast the robot can rotate in place.
         */
        // Here we calculate the theoretical maximum angular velocity. You can also replace this with a measured amount.
        // Freespeed: 61.21537703 rad/s^2
        // Calculated: 54.49885165 rad/s^2
        // Real: TODO rad/s^2 (after whole robot is assembled)
        public static final double MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND = MAX_VELOCITY_METERS_PER_SECOND /
        Math.hypot(DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0); // hypot is about 0.08116 meters
    }

    public static final class PigeonConstants {
        public static final int DRIVETRAIN_PIGEON_ID = 0;
    }
}
