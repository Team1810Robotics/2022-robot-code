package org.usd232.robotics.rapidreact.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;

import static org.usd232.robotics.rapidreact.Constants.CANCoderConstants;
import static org.usd232.robotics.rapidreact.Constants.DriveConstants;
import static org.usd232.robotics.rapidreact.Constants.ModuleConstants;
import static org.usd232.robotics.rapidreact.Constants.PigeonIMUConstants;
import org.usd232.robotics.rapidreact.log.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
    /**
     * The logger.
     * 
     * @since 2018
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = new Logger();
    
    private final SwerveModule frontLeft = new SwerveModule(
        DriveConstants.FRONT_LEFT_DRIVE_MOTOR_PORT,
        DriveConstants.FRONT_LEFT_TURNING_MOTOR_PORT,
        DriveConstants.FRONT_LEFT_DRIVE_MOTOR_REVERSED,
        DriveConstants.FRONT_LEFT_STEER_MOTOR_REVERSED,
        DriveConstants.FRONT_LEFT_MODULE_STEER_OFFSET,
        CANCoderConstants.FRONT_LEFT_MODULE_CANCODER,
        CANCoderConstants.FRONT_LEFT_CANCODER_REVERSED);

    private final SwerveModule frontRight = new SwerveModule(
        DriveConstants.FRONT_RIGHT_DRIVE_MOTOR_PORT,
        DriveConstants.FRONT_RIGHT_TURNING_MOTOR_PORT,
        DriveConstants.FRONT_RIGHT_DRIVE_MOTOR_REVERSED,
        DriveConstants.FRONT_RIGHT_STEER_MOTOR_REVERSED,
        DriveConstants.FRONT_RIGHT_MODULE_STEER_OFFSET,
        CANCoderConstants.FRONT_RIGHT_MODULE_CANCODER,
        CANCoderConstants.FRONT_RIGHT_CANCODER_REVERSED);

    private final SwerveModule backRight = new SwerveModule(
        DriveConstants.BACK_RIGHT_DRIVE_MOTOR_PORT,
        DriveConstants.BACK_RIGHT_TURNING_MOTOR_PORT,
        DriveConstants.BACK_RIGHT_DRIVE_MOTOR_REVERSED,
        DriveConstants.BACK_RIGHT_STEER_MOTOR_REVERSED,
        DriveConstants.BACK_RIGHT_MODULE_STEER_OFFSET,
        CANCoderConstants.BACK_RIGHT_MODULE_CANCODER,
        CANCoderConstants.BACK_RIGHT_CANCODER_REVERSED);

    private final SwerveModule backLeft = new SwerveModule(
        DriveConstants.BACK_LEFT_DRIVE_MOTOR_PORT,
        DriveConstants.BACK_LEFT_TURNING_MOTOR_PORT,
        DriveConstants.BACK_LEFT_DRIVE_MOTOR_REVERSED,
        DriveConstants.BACK_LEFT_STEER_MOTOR_REVERSED,
        DriveConstants.BACK_LEFT_MODULE_STEER_OFFSET,
        CANCoderConstants.BACK_LEFT_MODULE_CANCODER,
        CANCoderConstants.BACK_LEFT_CANCODER_REVERSED);

    private PigeonIMU m_gyro = new PigeonIMU(PigeonIMUConstants.ID);

    private final SwerveDriveOdometry m_odometer = new SwerveDriveOdometry(DriveConstants.DRIVE_KINEMATICS,
            new Rotation2d(0));

    /** Moves boot wait time for Pigeon to a diffrent thread to speed whole robot boot time */
    public DriveSubsystem() {
        new Thread(() -> {
            try {
                LOG.enter("Moved to Second Thread");
                Thread.sleep(6000);
                zeroHeading();
                LOG.leave("Exiting Second Thread");
            } catch (Exception e) {}
        }).start();
    }

    /** Zeros the Pigon */
    public void zeroHeading() {
        m_gyro.setFusedHeading(0.0);
    }

    /** @return the gyro heading as a double between 0 - 360 */
    public double getHeading() {
        return Math.IEEEremainder(m_gyro.getFusedHeading(), 360);
    }

    /** @return the Pigeon's Heading as a Rotation2d object
     * <p>
     * Used for WPI stuff usually
     */
    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getHeading());
    }

    /** @return a Pose2d object of the current pose mesured by the odometer 
     * <p>
     * Used for WPI stuff usually
     */
    public Pose2d getPose() {
        return m_odometer.getPoseMeters();
    }

    /** resets the Odometer to current position
     * @param pose Pose2d Object to reset to current position
     */
    public void resetOdometry(Pose2d pose) {
        m_odometer.resetPosition(pose, getRotation2d());
    }

    /** Updates the odometer and prints stuff to {@link SmartDashboard} */
    @Override
    public void periodic() {
        m_odometer.update(getRotation2d(), frontLeft.getState(), frontRight.getState(),
                            backLeft.getState(), backRight.getState());
        SmartDashboard.putNumber("Robot Heading", getHeading());
        SmartDashboard.putString("Robot Location", getPose().getTranslation().toString());
    }

    /** Stops all Modules */
    public void stopModules() {
        frontLeft.stop();
        frontRight.stop();
        backLeft.stop();
        backRight.stop();
    }

    /** Sets all Modules to a specified state
     * @param desiredStates array or {@link SwerveModuleState}s
    */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND);
        frontLeft.setDesiredState(desiredStates[0]);
        frontRight.setDesiredState(desiredStates[1]);
        backLeft.setDesiredState(desiredStates[2]);
        backRight.setDesiredState(desiredStates[3]);
    }
}
