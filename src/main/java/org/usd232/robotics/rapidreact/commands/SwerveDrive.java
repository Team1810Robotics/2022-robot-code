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

    /** get values for the robot drive
     * @param xSpeed Speed in meters per sec in the x-direction
     * @param ySpeed Speed in meters per sec in the y-direction
     * @param thetaSpeed Speed in Radians per sec for rotation of the robot
     * @param fieldOriented If the bot should drive in a field oriented manner
     */
    public SwerveDrive(DriveSubsystem driveSubsystem,
            Supplier<Double> xSpeed, Supplier<Double> ySpeed, Supplier<Double> thetaSpeed,
            Supplier<Boolean> fieldOriented) {

        this.m_driveSubsystem = driveSubsystem;
        this.m_xSpeed = xSpeed;
        this.m_ySpeed = ySpeed;
        this.m_thetaSpeed = thetaSpeed;
        this.m_fieldOriented = fieldOriented;

        // Point of a rate limiter: https://drive.google.com/file/d/1zfqB5pnWxgganCsLK0NAt5tgolMBcdBS/view?usp=sharing
        this.m_xLimiter = new SlewRateLimiter(DriveConstants.DRIVE_MAX_ACCELERATION);
        this.m_yLimiter = new SlewRateLimiter(DriveConstants.DRIVE_MAX_ACCELERATION);
        this.m_turningLimiter = new SlewRateLimiter(DriveConstants.DRIVE_MAX_ANGULAR_ACCELERATION);

        addRequirements(m_driveSubsystem);
    }
    
    @Override
    public void initialize() {/* EGG */}

    /** Takes the joystick values and applies them to robot <p>
     * {@inheritDoc}
    */
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

        // 3. Make the driving smoother 
        xSpeed = m_xLimiter.calculate(xSpeed) * DriveConstants.TELEOP_DRIVE_MAX_METERS_PER_SEC;
        ySpeed = m_yLimiter.calculate(ySpeed) * DriveConstants.TELEOP_DRIVE_MAX_METERS_PER_SEC;
        thetaSpeed = m_turningLimiter.calculate(thetaSpeed)
                        * DriveConstants.TELEOP_DRIVE_MAX_ANGULAR_RADIANS_PER_SEC;

        // 4. Construct desired chassis speeds
        ChassisSpeeds chassisSpeeds;

        chassisSpeeds = m_fieldOriented.get() ?
        // Relative to field 
        ChassisSpeeds.fromFieldRelativeSpeeds(
                xSpeed,
                ySpeed,
                thetaSpeed,
                m_driveSubsystem.getRotation2d()) :

        // Relative to robot
        new ChassisSpeeds(xSpeed,
                          ySpeed, 
                          thetaSpeed);

        // 5. Convert chassis speeds to individual module states
        SwerveModuleState[] moduleStates = DriveConstants.DRIVE_KINEMATICS.toSwerveModuleStates(chassisSpeeds);

        // 6. Output each module states to wheels
        m_driveSubsystem.setModuleStates(moduleStates);
    }

    /** After execute finishes isFinished gets run <p>
     * If isFinished returns false; execute will run again <p>
     * If isFinished returns true; end will run and stop the command<p>
     * {@inheritDoc}
     */
    @Override
    public boolean isFinished() {
        return false;
    }

    /** If interrupted is true, then perform the ending tasks for the command
     * <p>
     * In this case it stops the robot. 
     * Interrupted can be set to be true for many reasons
     * (e.g. robot turns off, another command needs to interrupt it, or isFinished returns true)<p>
     * {@inheritDoc}
     */
    @Override
    public void end(boolean interrupted) {
        m_driveSubsystem.stopModules();
    }
}
