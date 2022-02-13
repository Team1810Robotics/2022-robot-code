package org.usd232.robotics.rapidreact.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

import org.usd232.robotics.rapidreact.log.Logger;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

// https://drive.google.com/file/d/1U_pJGVHtfvDgoxIgC1ku_JjqnwX0gNPf/view?usp=sharing

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

        addRequirements(driveSubsystem);
    }

    /** Sets ChassisSpeeds to the x speed, y speed, and rotation */
    @Override
    public void execute() {
        // You can use `new ChassisSpeeds(...)` for robot-oriented movement instead of field-oriented movement
        if (fieldOriented) {
            m_driveSubsystem.drive(
                    ChassisSpeeds.fromFieldRelativeSpeeds(
                            m_translationXSupplier.getAsDouble(),
                            m_translationYSupplier.getAsDouble(),
                            m_rotationSupplier.getAsDouble(),
                            m_driveSubsystem.getGyroscopeRotation()));
        } else {
            m_driveSubsystem.drive(
                new ChassisSpeeds(
                    m_translationXSupplier.getAsDouble(),
                    m_translationYSupplier.getAsDouble(),
                    m_rotationSupplier.getAsDouble()));
        }
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
