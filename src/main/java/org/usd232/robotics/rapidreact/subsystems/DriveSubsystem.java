package org.usd232.robotics.rapidreact.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.usd232.robotics.rapidreact.Constants.DriveConstants;
import static org.usd232.robotics.rapidreact.Constants.PigeonConstants;
import static org.usd232.robotics.rapidreact.Constants.ModuleConstants;

public class DriveSubsystem extends SubsystemBase {

    private final static PigeonIMU m_pigeon = new PigeonIMU(PigeonConstants.DRIVETRAIN_PIGEON_ID);

    private final SwerveModule m_frontLeftModule;
    private final SwerveModule m_frontRightModule;
    private final SwerveModule m_backLeftModule;
    private final SwerveModule m_backRightModule;

    private ChassisSpeeds m_chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);

    /** Anytime an object of the DriveSubsystem class is called, this constructor sets up the swerve modules */
    public DriveSubsystem() {
        ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");
// Mom loves you
    m_frontLeftModule = Mk4SwerveModuleHelper.createFalcon500(
            // This parameter is optional, but will allow you to see the current state of the module on the dashboard.
        tab.getLayout("Front Left Module", BuiltInLayouts.kList)
            .withSize(2, 4)
            .withPosition(0, 0),
                // This can either be L1, L2, L3, or L4 depending on your gear configuration
                Mk4SwerveModuleHelper.GearRatio.L2,
                // This is the ID of the drive motor
                DriveConstants.FRONT_LEFT_MODULE_DRIVE_MOTOR,
                // This is the ID of the steer motor
                DriveConstants.FRONT_LEFT_MODULE_STEER_MOTOR,
                // This is the ID of the steer encoder
                DriveConstants.FRONT_LEFT_MODULE_STEER_CANCODER,
                // This is how much the steer encoder is offset from true zero (In our case, zero is facing straight forward)
                DriveConstants.FRONT_LEFT_MODULE_STEER_OFFSET);

    m_frontRightModule = Mk4SwerveModuleHelper.createFalcon500(
        tab.getLayout("Front Right Module", BuiltInLayouts.kList)
            .withSize(2, 4)
            .withPosition(2, 0),
                Mk4SwerveModuleHelper.GearRatio.L2,
                DriveConstants.FRONT_RIGHT_MODULE_DRIVE_MOTOR,
                DriveConstants.FRONT_RIGHT_MODULE_STEER_MOTOR,
                DriveConstants.FRONT_RIGHT_MODULE_STEER_CANCODER,
                DriveConstants.FRONT_RIGHT_MODULE_STEER_OFFSET);

    m_backLeftModule = Mk4SwerveModuleHelper.createFalcon500(
        tab.getLayout("Back Left Module", BuiltInLayouts.kList)
            .withSize(2, 4)
            .withPosition(4, 0),
                Mk4SwerveModuleHelper.GearRatio.L2,
                DriveConstants.BACK_LEFT_MODULE_DRIVE_MOTOR,
                DriveConstants.BACK_LEFT_MODULE_STEER_MOTOR,
                DriveConstants.BACK_LEFT_MODULE_STEER_CANCODER,
                DriveConstants.BACK_LEFT_MODULE_STEER_OFFSET);

    m_backRightModule = Mk4SwerveModuleHelper.createFalcon500(
        tab.getLayout("Back Right Module", BuiltInLayouts.kList)
            .withSize(2, 4)
            .withPosition(6, 0),
                Mk4SwerveModuleHelper.GearRatio.L2,
                DriveConstants.BACK_RIGHT_MODULE_DRIVE_MOTOR,
                DriveConstants.BACK_RIGHT_MODULE_STEER_MOTOR,
                DriveConstants.BACK_RIGHT_MODULE_STEER_CANCODER,
                DriveConstants.BACK_RIGHT_MODULE_STEER_OFFSET);
    }

    /**
     * Sets the gyroscope angle to zero. This can be used to set the direction the robot is currently facing to the
     * 'forwards' direction.
     */
    public void zeroGyroscope() {
        m_pigeon.setFusedHeading(0.0);
    }

    /** Grabs the rotation of the gyroscope */
    public Rotation2d getGyroscopeRotation() {
        return Rotation2d.fromDegrees(m_pigeon.getFusedHeading());
    }

    /** Smart Dashboard Variable */
    public static double getGyro() {
        return m_pigeon.getFusedHeading();
    }

    /** Very important function. Takes in the chassis speeds, then keeps them for later handling */
    public void drive(ChassisSpeeds chassisSpeeds) {
        m_chassisSpeeds = chassisSpeeds;
    }

    /** Periodically does stuff */
    @Override
    public void periodic() {
        SwerveModuleState[] states = DriveConstants.DRIVE_KINEMATICS.toSwerveModuleStates(m_chassisSpeeds);
        // Normalizes the wheel speeds, scaling the speed of all wheels down relative to each other. This just means that no one wheel will be going too fast when it shouldn't. 
        SwerveDriveKinematics.desaturateWheelSpeeds(states, ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND);

        // Set the motor speeds and rotation
        m_frontLeftModule.set(states[0].speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND * ModuleConstants.MAX_VOLTAGE, states[0].angle.getRadians());
        m_frontRightModule.set(states[1].speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND * ModuleConstants.MAX_VOLTAGE, states[1].angle.getRadians());
        m_backLeftModule.set(states[2].speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND * ModuleConstants.MAX_VOLTAGE, states[2].angle.getRadians());
        m_backRightModule.set(states[3].speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND * ModuleConstants.MAX_VOLTAGE, states[3].angle.getRadians());
    }
}
