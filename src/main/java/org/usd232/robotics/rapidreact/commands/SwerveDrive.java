package org.usd232.robotics.rapidreact.commands;

import java.util.function.Supplier;

import static org.usd232.robotics.rapidreact.Constants.DriveConstants;
import static org.usd232.robotics.rapidreact.Constants.OIConstants;

import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SwerveDrive extends CommandBase{
    
    private final DriveSubsystem m_driveSubsystem;
    private final Supplier<Double> m_xSpeed, m_ySpeed, m_thetaSpeed;
    private final Supplier<Boolean> m_fieldOriented;
    private final SlewRateLimiter m_xLimiter, m_yLimiter, m_turningLimiter;

    public SwerveDrive(DriveSubsystem driveSubsystem,
            Supplier<Double> xSpeed, Supplier<Double> ySpeed, Supplier<Double> thetaSpeed,
            Supplier<Boolean> fieldOriented) {

        this.m_driveSubsystem = driveSubsystem;
        this.m_xSpeed = xSpeed;
        this.m_ySpeed = ySpeed;
        this.m_thetaSpeed = thetaSpeed;
        this.m_fieldOriented = fieldOriented;

        this.m_xLimiter = new SlewRateLimiter(DriveConstants.DRIVE_MAX_ACCELERATION);
        this.m_yLimiter = new SlewRateLimiter(DriveConstants.DRIVE_MAX_ACCELERATION);
        this.m_turningLimiter = new SlewRateLimiter(DriveConstants.DRIVE_MAX_ANGULAR_ACCELERATION);

        addRequirements(m_driveSubsystem);
    }

    @Override
    public void initialize() {/* EGG */}

    @Override
    public void execute() {
        // 1. Get real-time joystick inputs
        double xSpeed = m_xSpeed.get();
        double ySpeed = m_ySpeed.get();
        double thetaSpeed = m_thetaSpeed.get();

        // 2. Apply deadband
        xSpeed = (Math.abs(xSpeed) > OIConstants.DEADBAND) ? xSpeed : 0.0;
        ySpeed = (Math.abs(ySpeed) > OIConstants.DEADBAND) ? ySpeed : 0.0;
        thetaSpeed = (Math.abs(thetaSpeed) > OIConstants.DEADBAND) ? thetaSpeed : 0.0;

        // 3. Make the driving smoother (https://drive.google.com/file/d/1zfqB5pnWxgganCsLK0NAt5tgolMBcdBS/view?usp=sharing)
        xSpeed = m_xLimiter.calculate(xSpeed) * DriveConstants.TELEOP_DRIVE_MAX_SPEED;
        ySpeed = m_yLimiter.calculate(ySpeed) * DriveConstants.TELEOP_DRIVE_MAX_SPEED;
        thetaSpeed = m_turningLimiter.calculate(thetaSpeed)
                        * DriveConstants.TELEOP_DRIVE_MAX_ANGULAR_SPEED;

        // 4. Construct desired chassis speeds
        ChassisSpeeds chassisSpeeds;
        if (m_fieldOriented.get()) {
            // Relative to field
            chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                    xSpeed, ySpeed, thetaSpeed, m_driveSubsystem.getRotation2d());
        } else {
            // Relative to robot
            chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, thetaSpeed);
        }

        // 5. Convert chassis speeds to individual module states
        SwerveModuleState[] moduleStates = DriveConstants.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);

        // 6. Output each module states to wheels
        m_driveSubsystem.setModuleStates(moduleStates);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_driveSubsystem.stopModules();
    }
}
