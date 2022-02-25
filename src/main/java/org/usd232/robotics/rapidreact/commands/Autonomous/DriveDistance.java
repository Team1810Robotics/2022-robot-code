package org.usd232.robotics.rapidreact.commands.Autonomous;

import org.usd232.robotics.rapidreact.commands.SwerveDrive;
import org.usd232.robotics.rapidreact.log.Logger;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveDistance extends CommandBase{
    /**
     * The logger.
     * 
     * @since 2018
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();
    
    private DriveSubsystem m_driveSubsystem;

    private double xDistance;
    private double yDistance;
    private double rotation;
    private double xSpeed;
    private double ySpeed;
    private double rotationSpeed;

    private final double rotationInitialValue;
    private final double xDistanceInitialValue;
    private final double yDistanceInitialValue;
    
    /** For Auto <p>
     * Drives specified distance
     * @param driveSubsystem The Subsystem that's required
     * @param xDistance The distance you would like to travel in the X direction [Meters]
     * @param yDistance The distance you would like to travel in the Y direction [Meters]
     * @param rotationAmount The rotation in degrees you would like the robot to have. [Degrees]
     * @param xSpeed The speed in m/s^2 that it should travel the X direction
     * @param ySpeed The speed in m/s^2 that it should travel the Y direction
     * @param rotationSpeed The speed in rads/s^2 that it should spin
     */
    public DriveDistance(DriveSubsystem driveSubsystem,
                        double xDistance,
                        double yDistance,
                        double rotationAmount,
                        double xSpeed,
                        double ySpeed,
                        double rotationSpeed) {
        this.m_driveSubsystem = driveSubsystem;
        this.xDistance = xDistance;
        this.yDistance = yDistance;
        this.rotation = ( rotationSpeed > 0.0 ) ? rotationAmount : 0.0;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.rotationSpeed = rotationSpeed;
        this.rotationInitialValue = Math.abs(DriveSubsystem.getGyro());
        this.xDistanceInitialValue = Math.abs(driveSubsystem.getTotalXTravel()); // TODO: This might not work.
        this.yDistanceInitialValue = Math.abs(driveSubsystem.getTotalYTravel()); // TODO: This also might not work.

        addRequirements(driveSubsystem);
    }

    /** For Auto <p>
     * Drives specified distance
     * @param driveSubsystem The Subsystem that's required
     * @param xDistance The distance you would like to travel in the X direction [Meters]
     * @param yDistance The distance you would like to travel in the Y direction [Meters]
     * @param rotation The rotation in degrees you would like the robot to have. [Degrees]
     */
    public DriveDistance(DriveSubsystem driveSubsystem,
                        double xDistance,
                        double yDistance,
                        double rotation) {
        this(driveSubsystem, xDistance, yDistance, rotation, 0.5, 0.5, 0.5);
    }

    /** For Auto <p>
     * Drives specified distance
     * @param driveSubsystem The Subsystem that's required
     * @param xDistance The distance you would like to travel in the X direction [Meters]
     * @param yDistance The distance you would like to travel in the Y direction [Meters]
     */
    public DriveDistance(DriveSubsystem driveSubsystem,
                        double xDistance,
                        double yDistance) {
        this(driveSubsystem, xDistance, yDistance, 0, 0.5, 0.5, 0);
    }

    @Override
    public void initialize() {
        LOG.enter("Started DriveDistance");
        /** Maybe not need or might be needed IDK */
        m_driveSubsystem.stopModules();
    }

    
    @Override
    public void execute() {
        if (DriveSubsystem.getGyro() - rotationInitialValue <= Math.abs(rotation)) {
            rotationSpeed = 0;
        }

        if (m_driveSubsystem.getTotalXTravel() - xDistanceInitialValue <= Math.abs(xDistance)) {
            xSpeed = 0;
        }

        if (m_driveSubsystem.getTotalYTravel() - yDistanceInitialValue <= Math.abs(yDistance)) {
            ySpeed = 0;
        }

        new SwerveDrive(this.m_driveSubsystem,
                    () -> xSpeed,
                    () -> ySpeed,
                    () -> rotationSpeed,
                    () -> true);
    } 

    @Override
    public boolean isFinished() {
        if (rotationSpeed == 0 && xSpeed == 0 && ySpeed == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_driveSubsystem.stopModules();
        LOG.leave("Stopping DriveDistance");
        SmartDashboard.putString("AutoDebug", "Drive Distance reached end");
    }
}
