package org.usd232.robotics.rapidreact;

import com.swervedrivespecialties.swervelib.SdsModuleConfigurations;

import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;

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
    public static final int FRONT_LEFT_DRIVE_MOTOR_PORT = 0;
    public static final int BACK_LEFT_DRIVE_MOTOR_PORT = 2;
    public static final int FRONT_RIGHT_DRIVE_MOTOR_PORT = 4;
    public static final int BACK_RIGHT_DRIVE_MOTOR_PORT = 6;

    public static final int FRONT_LEFT_TURNING_MOTOR_PORT = 1;
    public static final int BACK_LEFT_TURNING_MOTOR_PORT = 3;
    public static final int FRONT_RIGHT_TURNING_MOTOR_PORT = 5;
    public static final int BACK_RIGHT_TURNING_MOTOR_PORT = 7;

    public static final boolean FRONT_LEFT_TURNING_ENCODER_REVERSED = false;
    public static final boolean BACK_LEFT_TURNING_ENCODER_REVERSED = true;
    public static final boolean FRONT_RIGHT_TURNING_ENCODER_REVERSED = false;
    public static final boolean BACK_RIGHT_TURNING_ENCODER_REVERSED = true;

    public static final int[] FRONT_LEFT_DRIVE_ENCODER_PORTS = new int[] {7, 8};
    public static final int[] BACK_LEFT_DRIVE_ENCODER_PORTS = new int[] {9, 10};
    public static final int[] FRONT_RIGHT_DRIVE_ENCODER_PORTS = new int[] {11, 12};
    public static final int[] BACK_RIGHT_DRIVE_ENCODER_PORTS = new int[] {13, 14};

    public static final boolean FRONT_LEFT_DRIVE_ENCODER_REVERSED = false;
    public static final boolean BACK_LEFT_DRIVE_ENCODER_REVERSED = true;
    public static final boolean FRONT_RIGHT_DRIVE_ENCODER_REVERSED = false;
    public static final boolean BACK_RIGHT_DRIVE_ENCODER_REVERSED= true;

    public static final double FRONT_LEFT_MODULE_OFFSET = -Math.toRadians(0.0);
    public static final double FRONT_RIGHT_MODULE_OFFSET = -Math.toRadians(0.0);
    public static final double BACK_LEFT_MODULE_OFFSET = -Math.toRadians(0.0);
    public static final double BACK_RIGHT_MODULE_OFFSET = -Math.toRadians(0.0);

    /** Distance between centers of right and left wheels on robot */
    public static final double TRACK_WIDTH = 0.5;

    /** Distance between front and back wheels on robot */
    public static final double WHEEL_BASE = 0.7;

    public static final SwerveDriveKinematics DRIVE_KINEMATICS =
        new SwerveDriveKinematics(
            new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2),
            new Translation2d(WHEEL_BASE / 2, -TRACK_WIDTH / 2),
            new Translation2d(-WHEEL_BASE / 2, TRACK_WIDTH / 2),
            new Translation2d(-WHEEL_BASE / 2, -TRACK_WIDTH / 2));

    public static final boolean GYRO_REVERSED = false;
    
    public static final double MAX_VOLTAGE = 12.0;
    /* The formula for calculating the theoretical maximum velocity is:
     * <Motor free speed RPM> / 60 * <Drive reduction> * <Wheel diameter meters> * pi */
    public static final double MAX_VELOCITY_METERS_PER_SECOND = 6380.0 / 60.0 *
                               SdsModuleConfigurations.MK4_L2.getDriveReduction() *
                               SdsModuleConfigurations.MK4_L2.getWheelDiameter() * Math.PI;

    /**
     * The maximum angular velocity of the robot in radians per second.
     * <p>
     * This is a measure of how fast the robot can rotate in place.
     */
    // Here we calculate the theoretical maximum angular velocity. You can also replace this with a measured amount.
    public static final double MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND = MAX_VELOCITY_METERS_PER_SECOND /
                               Math.hypot(TRACK_WIDTH / 2.0, WHEEL_BASE / 2.0);

    }

  public static final class ModuleConstants {
    public static final double MAX_MODULE_ANGULAR_SPEED_RADIANS_PER_SECOND = 2 * Math.PI;
    public static final double MAX_MODULE_ANGULAR_ACCELERATION_RADIANS_PER_SECOND_SQUARED = 2 * Math.PI;

    public static final int ENCODER_CPR = 4096;
    public static final double WHEEL_DIAMETER_METERS = SdsModuleConfigurations.MK4_L2.getWheelDiameter();
    public static final double DRIVE_ENCODER_DISTANCE_PER_PULSE =
        // Assumes the encoders are directly mounted on the wheel shafts
        (WHEEL_DIAMETER_METERS * Math.PI) / (double) ENCODER_CPR;

    public static final double TURNING_ENCODER_DISTANCE_PER_PULSE =
        // Assumes the encoders are on a 1:1 reduction with the module shaft.
        (2 * Math.PI) / (double) ENCODER_CPR;
  }

  public static final class OIConstants {
    /**
     * The port for the left joystick.
     * 
     * @since 2022
     */
    public static final int LEFT_JOYSTICK_PORT = 0;
    /**
     * The port for the right joystick.
     * 
     * @since 2022
     */
    public static final int RIGHT_JOYSTICK_PORT = 1;
    /**
     * The port for the manipulator controller.
     * 
     * @since 2022
     */
    public static final int MANIPULATOR_PORT = 2;
  }

  public static final class AutoConstants {
    public static final double MAX_SPEED_METERS_PER_SECOND = 3;
    public static final double MAX_ACCELERATION_METERS_PER_SECOND_SQUARED = 3;
    public static final double MAX_ANGULAR_SPEED_RADIANS_PER_SECOND = Math.PI;
    public static final double MAX_ANGULAR_SPEED_RADIANS_PER_SECOND_SQUARED = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profilied robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
        new TrapezoidProfile.Constraints(
            MAX_ANGULAR_SPEED_RADIANS_PER_SECOND, MAX_ANGULAR_SPEED_RADIANS_PER_SECOND_SQUARED);
  }
  
  public static final class Gyro {
    public static final int PIGEONIMU_DEVICE_NUMBER = 0;
  }

  public static final class CANCoderConstants {
    public static final int FRONT_LEFT_CAN_CODER = 12;
    public static final int FRONT_RIGHT_CAN_CODER = 12;
    public static final int BACK_LEFT_CAN_CODER = 12;
    public static final int BACK_RIGHT_CAN_CODER = 12;
  }
}
