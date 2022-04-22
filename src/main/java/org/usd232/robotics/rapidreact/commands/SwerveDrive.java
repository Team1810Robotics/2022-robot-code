package org.usd232.robotics.rapidreact.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static org.usd232.robotics.rapidreact.Constants.*;
import org.usd232.robotics.rapidreact.log.Logger;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

// https://drive.google.com/file/d/1L3HFG1faKJ7LC5MNRQro7GX06wvOmHof/view?usp=sharing

public class SwerveDrive extends CommandBase {
    /**
     * The logger.
     * 
     * @since 2018
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();
    
    private final DriveSubsystem m_driveSubsystem;

    private final DoubleSupplier m_translationXSupplier;
    private final DoubleSupplier m_translationYSupplier;
    private final DoubleSupplier m_rotationSupplier;
    private final boolean fieldOriented;
    private final SlewRateLimiter xLimiter, yLimiter, thetaLimiter;

    /** Makes the robot drive.
    * @param X speed
    * @param Y speed
    * @param Rotation in Degrees */
    public SwerveDrive(DriveSubsystem driveSubsystem,
                               DoubleSupplier translationXSupplier,
                               DoubleSupplier translationYSupplier,
                               DoubleSupplier rotationSupplier,
                               boolean fieldOriented) {
        this.m_driveSubsystem = driveSubsystem;
        this.m_translationXSupplier = translationXSupplier;
        this.m_translationYSupplier = translationYSupplier;
        this.m_rotationSupplier = rotationSupplier;
        this.fieldOriented = fieldOriented;

        this.xLimiter = new SlewRateLimiter(ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND);
        this.yLimiter = new SlewRateLimiter(ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND);
        this.thetaLimiter = new SlewRateLimiter(ModuleConstants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND);

        addRequirements(driveSubsystem);
    }

    /** Sets ChassisSpeeds to the x speed, y speed, and rotation */
    @Override
    public void execute() {
        double xSpeed = m_translationXSupplier.getAsDouble();
        double ySpeed = m_translationYSupplier.getAsDouble();
        double thetaSpeed = m_rotationSupplier.getAsDouble();

        xSpeed = xLimiter.calculate(xSpeed) * DriveConstants.TELEOP_MAX_SPEED_PER_SEC;
        ySpeed = yLimiter.calculate(ySpeed) * DriveConstants.TELEOP_MAX_SPEED_PER_SEC;
        thetaSpeed = thetaLimiter.calculate(thetaSpeed)
                * DriveConstants.TELEOP_MAX_RADIANS_PER_SEC;

        // FIXME: remove
        xSpeed = (xSpeed < OIConstants.DEADBAND) ? 0.0 : xSpeed;
        ySpeed = (ySpeed < OIConstants.DEADBAND) ? 0.0 : ySpeed;
        thetaSpeed = (thetaSpeed < OIConstants.DEADBAND) ? 0.0 : thetaSpeed;

         m_driveSubsystem.drive( 
            (fieldOriented) ?
                ChassisSpeeds.fromFieldRelativeSpeeds(
                        xSpeed,
                        ySpeed,
                        thetaSpeed,
                        m_driveSubsystem.getGyroscopeRotation()) :

                new ChassisSpeeds(
                    xSpeed,
                    ySpeed,
                    thetaSpeed));
    }

    /** Checks if the conditions inside isFinished are true */
    @Override
    public boolean isFinished() {
        return false;
    }
    
    /** If interrupted is true, then stop the robot from moving */
    @Override
    public void end(boolean interrupted) {
        LOG.info("Swerve Drive Command ended");
        m_driveSubsystem.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
    }
}
