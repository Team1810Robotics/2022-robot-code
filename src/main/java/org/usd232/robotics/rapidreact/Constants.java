package org.usd232.robotics.rapidreact;

import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 * <p>
 * For example:
 * <pre> {@code import static org.usd232.robotics.rapidreact.Constants.*;} </pre>
 */
public final class Constants {
    // https://drive.google.com/file/d/1g1jBZHPf6Fq6V2tG7PFIGtjpEEV2BIGf/view?usp=sharing
    public static final class DriveConstants {
        /**
         * The left-to-right distance between the drive wheels
         *
         * Should be measured from center to center.
         */
        public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.5;
        
        /**
         * The front-to-back distance between the drive wheels.
         *
         * Should be measured from center to center.
        */
        public static final double DRIVETRAIN_WHEELBASE_METERS = 0.64928;

        // Front left swerve module
        public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 1;
        public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 2;
        public static final int FRONT_LEFT_MODULE_STEER_CANCODER = 10;
        public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(327.98);
        
        // Front right swerve module
        public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 3;
        public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 4;
        public static final int FRONT_RIGHT_MODULE_STEER_CANCODER = 9;
        public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(9.76);
        
        // Back left swerve module
        public static final int BACK_LEFT_MODULE_STEER_MOTOR = 7;
        public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 8;
        public static final int BACK_LEFT_MODULE_STEER_CANCODER = 12;
        public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(222.62);
        
        // Back right swerve module
        public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 5;
        public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 6;
        public static final int BACK_RIGHT_MODULE_STEER_CANCODER = 11;
        public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(93.60);

        public static final SwerveDriveKinematics DRIVE_KINEMATICS = new SwerveDriveKinematics(
          // Front left
          new Translation2d(DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Front right
          new Translation2d(DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Back left
          new Translation2d(-DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Back right
          new Translation2d(-DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0)
  );
    }
    
    public static final class ModuleConstants {
        /**
         * The maximum velocity of the robot in meters per second.
         * <p>
         * This is a measure of how fast the robot should be able to drive in a straight line.
         */
        // Freespeed: 4.96824 m/s^2
        // Calculated: 4.968230455 m/s^2
        // Real: (Not done) m/^2 (after whole robot is assembled)
        public static final double MAX_VELOCITY_METERS_PER_SECOND = 2048.0 / 60.0 *
        SdsModuleConfigurations.MK4_L2.getDriveReduction() *
        SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI;

        /**
         * The maximum voltage that will be delivered to the drive motors.
         * <p>
         * This can be reduced to cap the robot's maximum speed. Typically, this is useful during initial testing of the robot.
         */
        public static final double MAX_VOLTAGE = 5.0;

        /**
         * The maximum angular velocity of the robot in radians per second.
         * <p>
         * This is a measure of how fast the robot can rotate in place.
         */
        // Here we calculate the theoretical maximum angular velocity. You can also replace this with a measured amount.
        // Freespeed: 61.21537703 rad/s^2
        // Calculated: 61.2152594258 rad/s^2
        // Real: (Not done) rad/s^2 (after whole robot is assembled)
        public static final double MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND = MAX_VELOCITY_METERS_PER_SECOND /
        Math.hypot(DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0); // hypot is about 0.08116 meters
    }

    public static final class AutoConstants {
        public static final double MAX_AUTO_SPEED_PER_SEC = ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND / 1.5;
        public static final double MAX_AUTO_RADIANS_PER_SEC = ModuleConstants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND / 1.5;
        public static final double MAX_AUTO_ACCELERATION_RADIANS = 3;

        public static final double kp_X_CONTROLLER = 1.2; // (https://youtu.be/jIKBWO7ps0w)
        public static final double kp_Y_CONTROLLER = 1.2;
        public static final double kp_THETA_CONTROLLER = 2.8;

        public static final TrapezoidProfile.Constraints THETA_CONTROLLER_CONSTRAINTS = 
                new TrapezoidProfile.Constraints(
                        MAX_AUTO_RADIANS_PER_SEC,
                        MAX_AUTO_ACCELERATION_RADIANS);
    }

    public static final class OIConstants {
        public static final int MOVEMENT_JOYSTICK_PORT = 0;
        public static final int ROTATION_JOYSTICK_PORT = 1;
        public static final int MANIPULATOR_CONTROLLER_PORT = 2;

        public static final double DEADBAND = 0.4;
    }

    public static final class PigeonConstants {
        public static final int ID = 13;
    }

    public static final class PneumaticConstants {
        public static final int PH_CAN_ID = 17;

        public static final double MAX_TANK_PSI = 117;
        public static final double MIN_TANK_PSI = 110;
    }
    
    public static final class IntakeConstants {
        public static final int RIGHT_MOTOR_PORT = 1;
        public static final int LEFT_MOTOR_PORT = 0;

        public static final int LEFT_PNEUMATIC_PORT = 6;
        public static final int RIGHT_PNEUMATIC_PORT = 7;
    }

    public static final class ShooterConstants {
        public static final int MOTOR_PORT = 14;
    }

    public static final class HoodConstants {
        public static final int MOTOR_PORT = 2;
        public static final int HOOD_LIMIT_SWITCH_CHANNEL = 9;
        public static final int[] HOOD_ENCODER_CHANNEL = {1, 2};

        public static final double HOOD_DEADBAND = 200;

        public static double FORWARD_HOOD_LIMIT = -2650;
    }

    public static final class AugerConstants {
        public static final int VICTOR_ID = 18;
        public static final double AUGER_ON = -1.0;
    }
}