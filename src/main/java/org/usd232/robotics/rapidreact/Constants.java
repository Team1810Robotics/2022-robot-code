package org.usd232.robotics.rapidreact;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

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
        /** Distance between right and left wheels */
        public static final double TRACK_WIDTH = Units.inchesToMeters(19.6875);
        /** Distance between front and back wheels */
        public static final double WHEEL_BASE = Units.inchesToMeters(25.5625);

        public static final SwerveDriveKinematics DRIVE_KINEMATICS =
            new SwerveDriveKinematics(
                new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2),
                new Translation2d(WHEEL_BASE / 2, -TRACK_WIDTH / 2),
                new Translation2d(-WHEEL_BASE / 2, TRACK_WIDTH / 2),
                new Translation2d(-WHEEL_BASE / 2, -TRACK_WIDTH / 2));

        public static final int FRONT_LEFT_DRIVE_MOTOR_PORT = 1;
        public static final int FRONT_RIGHT_DRIVE_MOTOR_PORT = 3;
        public static final int BACK_RIGHT_DRIVE_MOTOR_PORT = 5;
        public static final int BACK_LEFT_DRIVE_MOTOR_PORT = 7;
   
        public static final int FRONT_LEFT_TURNING_MOTOR_PORT = 2;
        public static final int FRONT_RIGHT_TURNING_MOTOR_PORT = 4;
        public static final int BACK_RIGHT_TURNING_MOTOR_PORT = 6;
        public static final int BACK_LEFT_TURNING_MOTOR_PORT = 8;

        public static final int[] FRONT_LEFT_DRIVE_ENCODER = {0, 1}; // FIXME
        public static final int[] FRONT_RIGHT_DRIVE_ENCODER = {2, 3}; // FIXME
        public static final int[] BACK_LEFT_DRIVE_ENCODER = {4, 5}; // FIXME
        public static final int[] BACK_RIGHT_DRIVE_ENCODER = {6, 7}; // FIXME

        public static final boolean FRONT_LEFT_DRIVE_ENCODER_REVERSED = false;  //FIXME
        public static final boolean FRONT_RIGHT_DRIVE_ENCODER_REVERSED = false; //FIXME
        public static final boolean BACK_LEFT_DRIVE_ENCODER_REVERSED = false;  //FIXME
        public static final boolean BACK_RIGHT_DRIVE_ENCODER_REVERSED= false;  //FIXME

        public static final boolean FRONT_LEFT_DRIVE_MOTOR_REVERSED = false;  //FIXME
        public static final boolean FRONT_RIGHT_DRIVE_MOTOR_REVERSED = false; //FIXME
        public static final boolean BACK_LEFT_DRIVE_MOTOR_REVERSED = false;  //FIXME
        public static final boolean BACK_RIGHT_DRIVE_MOTOR_REVERSED= false;  //FIXME

        public static final boolean FRONT_LEFT_STEER_MOTOR_REVERSED = false;  //FIXME
        public static final boolean FRONT_RIGHT_STEER_MOTOR_REVERSED = false; //FIXME
        public static final boolean BACK_LEFT_STEER_MOTOR_REVERSED = false;  //FIXME
        public static final boolean BACK_RIGHT_STEER_MOTOR_REVERSED= false;  //FIXME

        public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(329.15);
        public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(9.76);
        public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(221.57);
        public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(91.58);

        public static final double TELEOP_DRIVE_MAX_SPEED = ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND / 4;
        public static final double TELEOP_DRIVE_MAX_ANGULAR_SPEED = ModuleConstants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND / 4;
        public static final double DRIVE_MAX_ACCELERATION = 3;
        public static final double DRIVE_MAX_ANGULAR_ACCELERATION = 3;
    }

    public static final class ModuleConstants {
        public static final double WHEEL_DIAMETER_METERS = Units.inchesToMeters(3.95);
        public static final double DRIVE_GEAR_RATIO = 6.75;
        public static final double STEER_GEAR_RATIO = 12.8;
        public static final double DRIVE_ENCODER_ROTATION_TO_METERS = DRIVE_GEAR_RATIO * Math.PI * WHEEL_DIAMETER_METERS;
        public static final double STEER_CANCODER_ROTATION_TO_RADS = STEER_GEAR_RATIO * 2 * Math.PI;
        public static final double DRIVE_ENCODER_RPM_TO_METERS_PER_SEC = DRIVE_ENCODER_ROTATION_TO_METERS / 2048;
        public static final double STEER_CANCODER_RPM_TO_RADS_PER_SEC = STEER_CANCODER_ROTATION_TO_RADS / 4096;
        public static final double kp_TURNING = 0.2;

        /**
         * The maximum velocity of the robot in meters per second.
         * <p>
         * This is a measure of how fast the robot should be able to drive in a straight line.
         */
        // Freespeed: 4.96824 m/s^2
        // Calculated: 4.4231268 m/s^2
        // Real: TODO m/^2 (after whole robot is assembled)
        public static final double MAX_VELOCITY_METERS_PER_SECOND = 5680.0 / 60.0 *
        ((14.0 / 50.0) * (27.0 / 17.0) * (15.0 / 45.0)) *
        WHEEL_DIAMETER_METERS * Math.PI;

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
        Math.hypot(DriveConstants.TRACK_WIDTH / 2.0, DriveConstants.WHEEL_BASE / 2.0); // hypot is about 0.08116 meters
    }


    public static final class AutoConstants {}

    public static final class OIConstants {
        public static final int MOVEMENT_JOYSTICK_PORT = 0;
        public static final int ROTATION_JOYSTICK_PORT = 1;
        public static final int MANIPULATOR_CONTROLLER_PORT = 3;

        public static final double DEADBAND = 0.25;
    }

    public static final class CANCoderConstants {
        public static final int FRONT_LEFT_MODULE_CANCODER = 10;
        public static final int FRONT_RIGHT_MODULE_CANCODER = 9;
        public static final int BACK_LEFT_MODULE_CANCODER = 12;
        public static final int BACK_RIGHT_MODULE_CANCODER = 11;

        public static final boolean FRONT_LEFT_CANCODER_REVERSED = false;
        public static final boolean FRONT_RIGHT_CANCODER_REVERSED = false;
        public static final boolean BACK_LEFT_CANCODER_REVERSED = false;
        public static final boolean BACK_RIGHT_CANCODER_REVERSED = false;
    }

    public static final class PigeonIMUConstants {
        public static final int ID = 0;
    }
}
