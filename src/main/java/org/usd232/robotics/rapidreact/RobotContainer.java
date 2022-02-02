package org.usd232.robotics.rapidreact;

import static org.usd232.robotics.rapidreact.Constants.AutoConstants;
import static org.usd232.robotics.rapidreact.Constants.OIConstants;

import java.util.List;

import org.usd232.robotics.rapidreact.Constants.DriveConstants;
import org.usd232.robotics.rapidreact.commands.SwerveDrive;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.Button;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();

    private final Joystick movJoystick = new Joystick(OIConstants.MOVEMENT_JOYSTICK_PORT);
    private final Joystick rotJoystick = new Joystick(OIConstants.ROTATION_JOYSTICK_PORT);

    public RobotContainer() {
        m_driveSubsystem.setDefaultCommand(new SwerveDrive(
            this.m_driveSubsystem,
            () -> -movJoystick.getX(),
            () -> -movJoystick.getY(),
            () -> -rotJoystick.getX(),
            () -> true));

        configureButtonBindings();
    }

    private void configureButtonBindings() {
        new Button(movJoystick::getTrigger).whenPressed(() -> m_driveSubsystem.zeroHeading()); // No requirements because we don't need to interrupt anything
        new Button(rotJoystick::getTrigger).whenPressed(() -> m_driveSubsystem.zeroHeading());
    }

    public Command getAutonomousCommand() {
    // 1. Create trajectory settings
        TrajectoryConfig trajectoryConfig = new TrajectoryConfig(
            AutoConstants.MAX_AUTO_SPEED_PER_SEC,
            AutoConstants.MAX_AUTO_RADIANS_PER_SEC)
                    .setKinematics(DriveConstants.DRIVE_KINEMATICS);
    
    // 2. Generate trajectory
    Trajectory exampleTrajectory = TrajectoryGenerator .generateTrajectory(
        new Pose2d(0, 0, new Rotation2d(0)),
            List.of(
                new Translation2d(1, 0),
                new Translation2d(1, -1)
            ),
        new Pose2d(2, -1, Rotation2d.fromDegrees(180)),
        trajectoryConfig
    );

    // 3. Define PID controllers for tracking trajectory
    PIDController xController = new PIDController(AutoConstants.kp_X_CONTROLLER, 0, 0);
    PIDController yController = new PIDController(AutoConstants.kp_Y_CONTROLLER, 0, 0);
    ProfiledPIDController thetaController = new ProfiledPIDController(
            AutoConstants.kp_THETA_CONTROLLER, 0.0, 0.1, AutoConstants.THETA_CONTROLLER_CONSTRAINTS);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    // 4. Construct command to follow trajectory
    SwerveControllerCommand swerveControllerCommand = new SwerveControllerCommand(
        exampleTrajectory,
        m_driveSubsystem::getPose,
        DriveConstants.DRIVE_KINEMATICS,
        xController,
        yController,
        thetaController,
        m_driveSubsystem::setModuleStates,
        m_driveSubsystem);
    
    // 5. Add some init and wrap-up, and return everything
    return new SequentialCommandGroup(
        new InstantCommand(() -> m_driveSubsystem.resetOdometry(exampleTrajectory.getInitialPose())),
        swerveControllerCommand,
        new InstantCommand(() -> m_driveSubsystem.stopModules()));
    }
}
