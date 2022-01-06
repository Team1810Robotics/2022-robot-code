package frc.robot.subsystems;

import frc.robot.Constants.CANCoderConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.log.Logger;
import frc.robot.Constants;

import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;

@SuppressWarnings("PMD.ExcessiveImports")
public class DriveSubsystem extends SubsystemBase {
    /**
     * The logger.
     * 
     * @since 2018
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = new Logger();

  // Robot swerve modules
  private final SwerveModule m_frontLeft;
  private final SwerveModule m_rearLeft;
  private final SwerveModule m_frontRight;
  private final SwerveModule m_rearRight;

  // The gyro sensor
  private final PigeonIMU m_gyro = 
  new PigeonIMU(Constants.Gyro.PIGEONIMU_DEVICE_NUMBER);

  private ChassisSpeeds m_chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);

  // Kinematics class for tracking robot kinematics
  private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
          // Front left
          new Translation2d(DriveConstants.TRACK_WIDTH / 2.0, DriveConstants.WHEEL_BASE / 2.0),
          // Front right
          new Translation2d(DriveConstants.TRACK_WIDTH / 2.0, -DriveConstants.WHEEL_BASE / 2.0),
          // Back left
          new Translation2d(-DriveConstants.TRACK_WIDTH / 2.0, DriveConstants.WHEEL_BASE / 2.0),
          // Back right
          new Translation2d(-DriveConstants.TRACK_WIDTH / 2.0, -DriveConstants.WHEEL_BASE / 2.0)
  );
  
  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");
// Mom loves you
    m_frontLeft = Mk4SwerveModuleHelper.createFalcon500(
        tab.getLayout("Front Left Module", BuiltInLayouts.kList)
              .withSize(2, 4)
              .withPosition(0, 0), 
        // This can be L1, L2, L3, or L4. We have L2      
        Mk4SwerveModuleHelper.GearRatio.L2,
        // This is the ID of the drive motor
        DriveConstants.FRONT_LEFT_DRIVE_MOTOR_PORT,
        // This is the ID of the steer motor 
        DriveConstants.FRONT_LEFT_TURNING_MOTOR_PORT,
        // This is the ID of the steer CANcoder 
        CANCoderConstants.FRONT_LEFT_CAN_CODER, 
        // This is how much the steer encoder is offset from true zero (In our case, zero is facing straight forward)
        DriveConstants.FRONT_LEFT_MODULE_OFFSET);

    m_rearLeft = Mk4SwerveModuleHelper.createFalcon500(
        tab.getLayout("Front Left Module", BuiltInLayouts.kList)
              .withSize(2, 4)
              .withPosition(4, 0),     
        Mk4SwerveModuleHelper.GearRatio.L2,
        DriveConstants.REAR_LEFT_DRIVE_MOTOR_PORT,
        DriveConstants.REAR_LEFT_TURNING_MOTOR_PORT,
        CANCoderConstants.REAR_LEFT_CAN_CODER, 
        DriveConstants.REAR_LEFT_MODULE_OFFSET);

    m_frontRight = Mk4SwerveModuleHelper.createFalcon500(
        tab.getLayout("Front Left Module", BuiltInLayouts.kList)
              .withSize(2, 4)
              .withPosition(2, 0),     
        Mk4SwerveModuleHelper.GearRatio.L2,
        DriveConstants.FRONT_RIGHT_DRIVE_MOTOR_PORT,
        DriveConstants.FRONT_RIGHT_TURNING_MOTOR_PORT,
        CANCoderConstants.FRONT_RIGHT_CAN_CODER, 
        DriveConstants.FRONT_RIGHT_MODULE_OFFSET);

    m_rearRight = Mk4SwerveModuleHelper.createFalcon500(
        tab.getLayout("Front Left Module", BuiltInLayouts.kList)
              .withSize(2, 4)
              .withPosition(6, 0),     
        Mk4SwerveModuleHelper.GearRatio.L2,
        DriveConstants.REAR_RIGHT_DRIVE_MOTOR_PORT,
        DriveConstants.REAR_RIGHT_TURNING_MOTOR_PORT,
        CANCoderConstants.REAR_RIGHT_CAN_CODER, 
        DriveConstants.REAR_RIGHT_MODULE_OFFSET);
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed Speed of the robot in the x direction (forward).
   * @param ySpeed Speed of the robot in the y direction (sideways).
   * @param rot Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   */
  @SuppressWarnings("ParameterName")
  public void drive(ChassisSpeeds chassisSpeeds) {
    this.m_chassisSpeeds = chassisSpeeds;
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    m_gyro.setFusedHeading(0.0);
  }

  public Rotation2d getGyroscopeRotation() {
    return Rotation2d.fromDegrees(m_gyro.getFusedHeading());
  }

  @Override
  public void periodic() {
    SwerveModuleState[] states = m_kinematics.toSwerveModuleStates(m_chassisSpeeds);
    SwerveDriveKinematics.normalizeWheelSpeeds(states, DriveConstants.MAX_VELOCITY_METERS_PER_SECOND);

    m_frontLeft.set(states[0].speedMetersPerSecond / DriveConstants.MAX_VELOCITY_METERS_PER_SECOND * 
                    DriveConstants.MAX_VOLTAGE, states[0].angle.getRadians());
    m_frontRight.set(states[1].speedMetersPerSecond / DriveConstants.MAX_VELOCITY_METERS_PER_SECOND * 
                     DriveConstants.MAX_VOLTAGE, states[1].angle.getRadians());
    m_rearLeft.set(states[2].speedMetersPerSecond / DriveConstants.MAX_VELOCITY_METERS_PER_SECOND * 
                   DriveConstants.MAX_VOLTAGE, states[2].angle.getRadians());
    m_rearRight.set(states[3].speedMetersPerSecond / DriveConstants.MAX_VELOCITY_METERS_PER_SECOND * 
                   DriveConstants.MAX_VOLTAGE, states[3].angle.getRadians());
  }
}