package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.*;
import org.usd232.robotics.rapidreact.log.Logger;
//import org.usd232.robotics.rapidreact.subsystems.SwerveModule;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.PigeonIMU;

@SuppressWarnings("PMD.ExcessiveImports")
public class DriveSubsystem extends SubsystemBase {
  @SuppressWarnings("unused")
  /**
   * The logger.
   * 
   * @since 2018
   */
  private static final Logger LOG = new Logger();

  // Robot swerve modules
  private final SwerveModule m_frontLeft;
  private final SwerveModule m_backLeft;
  private final SwerveModule m_frontRight;
  private final SwerveModule m_backRight;

  // CANCoder Objects
  private final CANCoder m_frontLeftCanCoder  = new CANCoder(CANCoderConstants.FRONT_LEFT_CAN_CODER);
  private final CANCoder m_backLeftCanCoder   = new CANCoder(CANCoderConstants.BACK_LEFT_CAN_CODER);
  private final CANCoder m_frontRightCanCoder = new CANCoder(CANCoderConstants.FRONT_RIGHT_CAN_CODER);
  private final CANCoder m_backRightCanCoder  = new CANCoder(CANCoderConstants.BACK_RIGHT_CAN_CODER);

  // Drive Encoders
  private final Encoder m_frontLeftDriveEncoder  = new Encoder(DriveConstants.FRONT_LEFT_DRIVE_ENCODER_PORTS[0],  DriveConstants.FRONT_LEFT_DRIVE_ENCODER_PORTS[1]);
  private final Encoder m_backLeftDriveEncoder   = new Encoder(DriveConstants.BACK_LEFT_DRIVE_ENCODER_PORTS[0],   DriveConstants.BACK_LEFT_DRIVE_ENCODER_PORTS[1]);
  private final Encoder m_frontRightDriveEncoder = new Encoder(DriveConstants.FRONT_RIGHT_DRIVE_ENCODER_PORTS[0], DriveConstants.FRONT_RIGHT_DRIVE_ENCODER_PORTS[1]);
  private final Encoder m_backRightDriveEncoder  = new Encoder(DriveConstants.BACK_RIGHT_DRIVE_ENCODER_PORTS[0],  DriveConstants.BACK_RIGHT_DRIVE_ENCODER_PORTS[1]);

  // The gyro sensor
  private final PigeonIMU m_gyro = new PigeonIMU(Gyro.PIGEONIMU_DEVICE_NUMBER);

  private ChassisSpeeds m_chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);

  //private final SwerveModule[] m_modules;
  private final Encoder[] m_driveEncoders;
  private final CANCoder[] m_canCoders;

  private static final int module_frontLeft = 0;
  private static final int module_frontRight = 1;
  private static final int module_backLeft = 2;
  private static final int module_backRight = 3;

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
  
  private final SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(m_kinematics,
  getGyroscopeRotation(), new Pose2d(5.0, 13.5, new Rotation2d()));

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
// Mom loves you
    this.m_frontLeft = new SwerveModule(DriveConstants.FRONT_LEFT_DRIVE_MOTOR_PORT,
                                        DriveConstants.FRONT_LEFT_TURNING_MOTOR_PORT,
                                        DriveConstants.FRONT_LEFT_DRIVE_ENCODER_PORTS[0],
                                        DriveConstants.FRONT_LEFT_DRIVE_ENCODER_PORTS[1],
                                        CANCoderConstants.FRONT_LEFT_CAN_CODER);

    this.m_frontRight = new SwerveModule(DriveConstants.FRONT_RIGHT_DRIVE_MOTOR_PORT,
                                         DriveConstants.FRONT_RIGHT_TURNING_MOTOR_PORT,
                                         DriveConstants.FRONT_RIGHT_DRIVE_ENCODER_PORTS[0],
                                         DriveConstants.FRONT_RIGHT_DRIVE_ENCODER_PORTS[1],
                                         CANCoderConstants.FRONT_RIGHT_CAN_CODER);

    this.m_backLeft = new SwerveModule(DriveConstants.BACK_LEFT_DRIVE_MOTOR_PORT,
                                       DriveConstants.BACK_LEFT_TURNING_MOTOR_PORT,
                                       DriveConstants.BACK_LEFT_DRIVE_ENCODER_PORTS[0],
                                       DriveConstants.BACK_LEFT_DRIVE_ENCODER_PORTS[1],
                                       CANCoderConstants.BACK_LEFT_CAN_CODER);

    this.m_backRight = new SwerveModule(DriveConstants.BACK_RIGHT_DRIVE_MOTOR_PORT,
                                       DriveConstants.BACK_RIGHT_TURNING_MOTOR_PORT,
                                       DriveConstants.BACK_RIGHT_DRIVE_ENCODER_PORTS[0],
                                       DriveConstants.BACK_RIGHT_DRIVE_ENCODER_PORTS[1],
                                       CANCoderConstants.BACK_RIGHT_CAN_CODER);

     //m_modules = new SwerveModule[]{m_frontLeft, m_frontRight, m_backLeft, m_backRight};

     this.m_driveEncoders = new Encoder[]{m_frontLeftDriveEncoder, m_frontRightDriveEncoder,
                                     m_backLeftDriveEncoder, m_backRightDriveEncoder};

     this.m_canCoders = new CANCoder[]{m_frontLeftCanCoder, m_frontRightCanCoder,
                                  m_backLeftCanCoder, m_backRightCanCoder};
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
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    var swerveModuleStates =
    DriveConstants.DRIVE_KINEMATICS.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rot, getGyroscopeRotation())
            : new ChassisSpeeds(xSpeed, ySpeed, rot));
    SwerveDriveKinematics.normalizeWheelSpeeds(
        swerveModuleStates, DriveConstants.MAX_VELOCITY_METERS_PER_SECOND);
    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_backLeft.setDesiredState(swerveModuleStates[2]);
    m_backRight.setDesiredState(swerveModuleStates[3]);
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    m_gyro.setFusedHeading(0.0);
  }

  /** Gets the gyro rotation in a way that {@link Rotation2d} and use */
  public Rotation2d getGyroscopeRotation() {
    return Rotation2d.fromDegrees(m_gyro.getFusedHeading());
  }

  /** Returns the position of the canCoder for specified module
   * 
   * @param canCoder 0 = Front Left; 1 = Front Right; 2 = Back Left; 3 = Back Right;
   */
  public double getCanCoderPosition(int canCoder) {
    return m_canCoders[canCoder].getPosition();
  }

  /** Smartdashboard Method */
  public double getGyroAngle() {
    return m_gyro.getFusedHeading();
  }

  public void resetOdometry(Pose2d pose){
    m_odometry.resetPosition(pose, getGyroscopeRotation());
  }

  public void resetOdometry(Pose2d pose, Rotation2d rotation) {
    m_odometry.resetPosition(pose, rotation);
  }

  /**
   * Returns the position of the robot on the field.
   *
   * @return the pose of the robot (x and y ane in meters)
   */
  public Pose2d getPoseMeters() {
    return m_odometry.getPoseMeters();
  }

  /*public Pose2d[] getModulePoses() {
    Pose2d[] modulePoses = {
        m_frontLeft.getPoseMeters(),
        m_frontRight.getPoseMeters(),
        m_backLeft.getPoseMeters(),
        m_backRight.getPoseMeters()
    };
    return modulePoses;
}*/

  /** Resets all drive Encoders */
  public void resetDriveEncoders() {
    m_driveEncoders[module_frontLeft].reset();  m_driveEncoders[module_frontRight].reset();
    m_driveEncoders[module_backLeft].reset();   m_driveEncoders[module_backRight].reset();
  }

  /**
   * Resets specified drive encoder
   * 
   * @param encoderNumber 0 = Front Left; 1 = Front Right; 2 = Back Left; 3 = Back Right;
   */
  public void resetDriveEncoders(int encoderNumber) {
    m_driveEncoders[encoderNumber].reset();
  }

  /**
   * Sets the swerve ModuleStates.
   *
   * @param desiredStates The desired SwerveModule states.
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.normalizeWheelSpeeds(
        desiredStates, DriveConstants.MAX_VELOCITY_METERS_PER_SECOND);
    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_backLeft.setDesiredState(desiredStates[2]);
    m_backRight.setDesiredState(desiredStates[3]);
  }

  @Override
  public void periodic() {
    var gyroAngle = getGyroscopeRotation();


    m_odometry.update(gyroAngle, m_frontLeft.getState(),   m_frontRight.getState(),
                                 m_backLeft.getState(),    m_backRight.getState());

    SwerveModuleState[] states = m_kinematics.toSwerveModuleStates(m_chassisSpeeds);
    SwerveDriveKinematics.normalizeWheelSpeeds(states, DriveConstants.MAX_VELOCITY_METERS_PER_SECOND);

    /*m_frontLeft.set(states[0].speedMetersPerSecond / DriveConstants.MAX_VELOCITY_METERS_PER_SECOND * 
                    DriveConstants.MAX_VOLTAGE, states[0].angle.getRadians());

    m_frontRight.set(states[1].speedMetersPerSecond / DriveConstants.MAX_VELOCITY_METERS_PER_SECOND * 
                     DriveConstants.MAX_VOLTAGE, states[1].angle.getRadians());

    m_backLeft.set(states[2].speedMetersPerSecond / DriveConstants.MAX_VELOCITY_METERS_PER_SECOND * 
                   DriveConstants.MAX_VOLTAGE, states[2].angle.getRadians());

    m_backRight.set(states[3].speedMetersPerSecond / DriveConstants.MAX_VELOCITY_METERS_PER_SECOND * 
                   DriveConstants.MAX_VOLTAGE, states[3].angle.getRadians());*/
  }
}