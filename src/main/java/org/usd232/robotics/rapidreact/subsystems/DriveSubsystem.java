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
            DriveConstants.FRONT_LEFT_DRIVE_ENCODER[0],
            DriveConstants.FRONT_LEFT_DRIVE_ENCODER[1],
            DriveConstants.FRONT_LEFT_MODULE_STEER_OFFSET,
            CANCoderConstants.FRONT_LEFT_MODULE_CANCODER,
            CANCoderConstants.FRONT_LEFT_CANCODER_REVERSED);

    private final SwerveModule frontRight = new SwerveModule(
        DriveConstants.FRONT_RIGHT_DRIVE_MOTOR_PORT,
        DriveConstants.FRONT_RIGHT_TURNING_MOTOR_PORT,
        DriveConstants.FRONT_RIGHT_DRIVE_MOTOR_REVERSED,
        DriveConstants.FRONT_RIGHT_STEER_MOTOR_REVERSED,
        DriveConstants.FRONT_RIGHT_DRIVE_ENCODER[0],
        DriveConstants.FRONT_RIGHT_DRIVE_ENCODER[1],
        DriveConstants.FRONT_RIGHT_MODULE_STEER_OFFSET,
        CANCoderConstants.FRONT_RIGHT_MODULE_CANCODER,
        CANCoderConstants.FRONT_RIGHT_CANCODER_REVERSED);

    private final SwerveModule backLeft = new SwerveModule(
        DriveConstants.BACK_LEFT_DRIVE_MOTOR_PORT,
        DriveConstants.BACK_LEFT_TURNING_MOTOR_PORT,
        DriveConstants.BACK_LEFT_DRIVE_MOTOR_REVERSED,
        DriveConstants.BACK_LEFT_STEER_MOTOR_REVERSED,
        DriveConstants.BACK_LEFT_DRIVE_ENCODER[0],
        DriveConstants.BACK_LEFT_DRIVE_ENCODER[1],
        DriveConstants.BACK_LEFT_MODULE_STEER_OFFSET,
        CANCoderConstants.BACK_LEFT_MODULE_CANCODER,
        CANCoderConstants.BACK_LEFT_CANCODER_REVERSED);

    private final SwerveModule backRight = new SwerveModule(
        DriveConstants.BACK_RIGHT_DRIVE_MOTOR_PORT,
        DriveConstants.BACK_RIGHT_TURNING_MOTOR_PORT,
        DriveConstants.BACK_RIGHT_DRIVE_MOTOR_REVERSED,
        DriveConstants.BACK_RIGHT_STEER_MOTOR_REVERSED,
        DriveConstants.BACK_RIGHT_DRIVE_ENCODER[0],
        DriveConstants.BACK_RIGHT_DRIVE_ENCODER[1],
        DriveConstants.BACK_RIGHT_MODULE_STEER_OFFSET,
        CANCoderConstants.BACK_RIGHT_MODULE_CANCODER,
        CANCoderConstants.BACK_RIGHT_CANCODER_REVERSED);

    private PigeonIMU gyro = new PigeonIMU(PigeonIMUConstants.ID);

    private final SwerveDriveOdometry m_odometer = new SwerveDriveOdometry(DriveConstants.DRIVE_KINEMATICS,
            new Rotation2d(0));

    public DriveSubsystem() {
        new Thread(() -> {
            try {
                Thread.sleep(6000);
                zeroHeading();
            } catch (Exception e) {}
        }).start();
        
    }

    public void zeroHeading() {
        gyro.setFusedHeading(0);
    }

    public double getHeading() {
        return Math.IEEEremainder(gyro.getYaw(), 360);
    }

    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getHeading());
    }

    public Pose2d getPose() {
        return m_odometer.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        m_odometer.resetPosition(pose, getRotation2d());
    }

    @Override
    public void periodic() {
        m_odometer.update(getRotation2d(), frontLeft.getState(), frontRight.getState(),
                            backLeft.getState(), backRight.getState());
        SmartDashboard.putNumber("Robot Heading", getHeading());
        SmartDashboard.putString("Robot Location", getPose().getTranslation().toString());
    }

    public void stopModules() {
        frontLeft.stop();
        frontRight.stop();
        backLeft.stop();
        backRight.stop();
    }

    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND);
        frontLeft.setDesiredState(desiredStates[0]);
        frontRight.setDesiredState(desiredStates[1]);
        backLeft.setDesiredState(desiredStates[2]);
        backRight.setDesiredState(desiredStates[3]);
    }
}
