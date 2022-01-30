// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.DriveConstants;
import static frc.robot.Constants.PigeonConstants;
import static frc.robot.Constants.ModuleConstants;

public class DriveSubsystem extends SubsystemBase {
        
  private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
          // Front left
          new Translation2d(DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Front right
          new Translation2d(DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Back left
          new Translation2d(-DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0),
          // Back right
          new Translation2d(-DriveConstants.DRIVETRAIN_TRACKWIDTH_METERS / 2.0, -DriveConstants.DRIVETRAIN_WHEELBASE_METERS / 2.0)
  );

  // By default we use a Pigeon for our gyroscope. But if you use another gyroscope, like a NavX, you can change this.
  // The important thing about how you configure your gyroscope is that rotating the robot counter-clockwise should
  // cause the angle reading to increase until it wraps back over to zero.
  private final PigeonIMU m_pigeon = new PigeonIMU(PigeonConstants.DRIVETRAIN_PIGEON_ID);

  private final SwerveModule m_frontLeftModule;
  private final SwerveModule m_frontRightModule;
  private final SwerveModule m_backLeftModule;
  private final SwerveModule m_backRightModule;

  private ChassisSpeeds m_chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);

  public DriveSubsystem() {
    ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");
// Mom loves you
    m_frontLeftModule = Mk4SwerveModuleHelper.createFalcon500(
            // This parameter is optional, but will allow you to see the current state of the module on the dashboard.
            tab.getLayout("Front Left Module", BuiltInLayouts.kList)
                    .withSize(2, 4)
                    .withPosition(0, 0),
            // This can either be STANDARD or FAST depending on your gear configuration
            Mk4SwerveModuleHelper.GearRatio.L2,
            // This is the ID of the drive motor
            DriveConstants.FRONT_LEFT_MODULE_DRIVE_MOTOR,
            // This is the ID of the steer motor
            DriveConstants.FRONT_LEFT_MODULE_STEER_MOTOR,
            // This is the ID of the steer encoder
            DriveConstants.FRONT_LEFT_MODULE_STEER_ENCODER,
            // This is how much the steer encoder is offset from true zero (In our case, zero is facing straight forward)
            DriveConstants.FRONT_LEFT_MODULE_STEER_OFFSET);

    m_frontRightModule = Mk4SwerveModuleHelper.createFalcon500(
            tab.getLayout("Front Right Module", BuiltInLayouts.kList)
                    .withSize(2, 4)
                    .withPosition(2, 0),
            Mk4SwerveModuleHelper.GearRatio.L2,
            DriveConstants.FRONT_RIGHT_MODULE_DRIVE_MOTOR,
            DriveConstants.FRONT_RIGHT_MODULE_STEER_MOTOR,
            DriveConstants.FRONT_RIGHT_MODULE_STEER_ENCODER,
            DriveConstants.FRONT_RIGHT_MODULE_STEER_OFFSET);

    m_backLeftModule = Mk4SwerveModuleHelper.createFalcon500(
            tab.getLayout("Back Left Module", BuiltInLayouts.kList)
                    .withSize(2, 4)
                    .withPosition(4, 0),
            Mk4SwerveModuleHelper.GearRatio.L2,
            DriveConstants.BACK_LEFT_MODULE_DRIVE_MOTOR,
            DriveConstants.BACK_LEFT_MODULE_STEER_MOTOR,
            DriveConstants.BACK_LEFT_MODULE_STEER_ENCODER,
            DriveConstants.BACK_LEFT_MODULE_STEER_OFFSET);

    m_backRightModule = Mk4SwerveModuleHelper.createFalcon500(
            tab.getLayout("Back Right Module", BuiltInLayouts.kList)
                    .withSize(2, 4)
                    .withPosition(6, 0),
            Mk4SwerveModuleHelper.GearRatio.L2,
            DriveConstants.BACK_RIGHT_MODULE_DRIVE_MOTOR,
            DriveConstants.BACK_RIGHT_MODULE_STEER_MOTOR,
            DriveConstants.BACK_RIGHT_MODULE_STEER_ENCODER,
            DriveConstants.BACK_RIGHT_MODULE_STEER_OFFSET);
  }

  /**
   * Sets the gyroscope angle to zero. This can be used to set the direction the robot is currently facing to the
   * 'forwards' direction.
   */
  public void zeroGyroscope() {
    m_pigeon.setFusedHeading(0.0);
  }

  public Rotation2d getGyroscopeRotation() {
    return Rotation2d.fromDegrees(m_pigeon.getFusedHeading());
  }

  public void drive(ChassisSpeeds chassisSpeeds) {
    m_chassisSpeeds = chassisSpeeds;
  }

  @Override
  public void periodic() {
    SwerveModuleState[] states = m_kinematics.toSwerveModuleStates(m_chassisSpeeds);
    SwerveDriveKinematics.desaturateWheelSpeeds(states, ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND);

    m_frontLeftModule.set(states[0].speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND * ModuleConstants.MAX_VOLTAGE, states[0].angle.getRadians());
    m_frontRightModule.set(states[1].speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND * ModuleConstants.MAX_VOLTAGE, states[1].angle.getRadians());
    m_backLeftModule.set(states[2].speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND * ModuleConstants.MAX_VOLTAGE, states[2].angle.getRadians());
    m_backRightModule.set(states[3].speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND * ModuleConstants.MAX_VOLTAGE, states[3].angle.getRadians());
  }
}
