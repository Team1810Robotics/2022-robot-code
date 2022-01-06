package org.usd232.robotics.rapidreact;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import static org.usd232.robotics.rapidreact.Constants.DriveConstants;
import static org.usd232.robotics.rapidreact.Constants.OIConstants;

import org.usd232.robotics.rapidreact.Constants.AutoConstants;
import org.usd232.robotics.rapidreact.commands.SwerveDriveCommand;
import org.usd232.robotics.rapidreact.log.Logger;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import java.util.List;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  /**
   * The logger.
   * 
   * @since 2018
   */
  @SuppressWarnings("unused")
  private static final Logger LOG = new Logger();
  
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();


  // Left Movment Cotroller
  private final Joystick m_movementJoystick = new Joystick(OIConstants.LEFT_JOYSTICK_PORT);
  // Right Rotation Controller
  private final Joystick m_rotationalJoystick = new Joystick(OIConstants.RIGHT_JOYSTICK_PORT);

  // Manipulator controller
  private final XboxController m_manipulatorController = new XboxController(OIConstants.MANIPULATOR_PORT);


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Set up the default command for the drivetrain.
    // The controls are for field-oriented driving:
    // Left stick Y axis -> forward and backwards movement
    // Left stick X axis -> left and right movement
    // Right stick X axis -> rotation
    m_robotDrive.setDefaultCommand(new SwerveDriveCommand(
            m_robotDrive,
            () -> -modifyAxis(m_movementJoystick.getY(GenericHID.Hand.kLeft)) * DriveConstants.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_movementJoystick.getX(GenericHID.Hand.kLeft)) * DriveConstants.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_rotationalJoystick.getX(GenericHID.Hand.kRight)) * DriveConstants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
    ));

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Top button zeros the gyroscope
    new Button(m_manipulatorController::getBackButton).whenPressed(m_robotDrive::zeroHeading);   // No requirements because we don't need to interrupt anything
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {

    // Create config for trajectory
    TrajectoryConfig config =
        new TrajectoryConfig(
                AutoConstants.MAX_SPEED_METERS_PER_SECOND,
                AutoConstants.MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(DriveConstants.DRIVE_KINEMATICS);

    // An example trajectory to follow.  All units in meters.
    Trajectory exampleTrajectory =
        TrajectoryGenerator.generateTrajectory(
            // Start at the origin facing the +X direction
            new Pose2d(0, 0, new Rotation2d(0)),
            // Pass through these two interior waypoints, making an 's' curve path
            List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
            // End 3 meters straight ahead of where we started, facing forward
            new Pose2d(3, 0, new Rotation2d(0)),
            config);


    /** Umbrella Trajectory Visulization
     * https://drive.google.com/file/d/1oRpwh6OS0N83WOusfB518qmYpUqniOW7/view?usp=sharing
     */
    Trajectory Umbrella = 
            TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(-0.5, 0.5), new Translation2d(-1, 0),
                    new Translation2d(-1.5, 0.5), new Translation2d(-2, 0),
                    new Translation2d(-2.5, 0.5), new Translation2d(-3, 0),
                    new Translation2d(-3.5, 0.5), new Translation2d(-4, 0),
                    new Translation2d(-4.5, 0.5), new Translation2d(-5, 0),
                    new Translation2d(-5.5, 0.5), new Translation2d(-6, 0),
                    new Translation2d(-6.5, 0.5), new Translation2d(-7, 0),
                    new Translation2d(-7.5, 0.5), new Translation2d(-8, 0),
                    
                    //Major Arc back to (0,0)
                    //new Translation2d(6.83, 2.83), 
                    new Translation2d(-4, 4)//, 
                    //new Translation2d(1.17, 2.83)
                    ),
            new Pose2d(0, 0, new Rotation2d(0)),
            config);

    Trajectory HalfSizedUmbrella =
            TrajectoryGenerator.generateTrajectory(
              new Pose2d(0, 0, new Rotation2d(0)), 
              List.of(new Translation2d(0.5 / 2, 0.5 / 2), new Translation2d(1 / 2, 0),
                      new Translation2d(1.5 / 2, 0.5 / 2), new Translation2d(2 / 2, 0),
                      new Translation2d(2.5 / 2, 0.5 / 2), new Translation2d(3 / 2, 0),
                      new Translation2d(3.5 / 2, 0.5 / 2), new Translation2d(4 / 2, 0),
                      new Translation2d(4.5 / 2, 0.5 / 2), new Translation2d(5 / 2, 0),
                      new Translation2d(5.5 / 2, 0.5 / 2), new Translation2d(6 / 2, 0),
                      new Translation2d(6.5 / 2, 0.5 / 2), new Translation2d(7 / 2, 0),
                      new Translation2d(7.5 / 2, 0.5 / 2), new Translation2d(8 / 2, 0),
              
                      //Major Arc back to (0,0)
                      //new Translation2d(6.83, 2.83), 
                      new Translation2d(4, 4)//, 
                      //new Translation2d(1.17, 2.83)
                      ),
              new Pose2d(0, 0, new Rotation2d(0)), 
              config);

    Trajectory FourthSizedUmbrella = // Prob wont work because its too small
            TrajectoryGenerator.generateTrajectory(
              new Pose2d(0, 0, new Rotation2d(0)), 
              List.of(new Translation2d(0.5 / 4, 0.5 / 4), new Translation2d(1 / 4, 0),
                      new Translation2d(1.5 / 4, 0.5 / 4), new Translation2d(2 / 4, 0),
                      new Translation2d(2.5 / 4, 0.5 / 4), new Translation2d(3 / 4, 0),
                      new Translation2d(3.5 / 4, 0.5 / 4), new Translation2d(4 / 4, 0),
                      new Translation2d(4.5 / 4, 0.5 / 4), new Translation2d(5 / 4, 0),
                      new Translation2d(5.5 / 4, 0.5 / 4), new Translation2d(6 / 4, 0),
                      new Translation2d(6.5 / 4, 0.5 / 4), new Translation2d(7 / 4, 0),
                      new Translation2d(7.5 / 4, 0.5 / 4), new Translation2d(8 / 4, 0),
                    
                      //Major Arc back to (0,0)
                      //new Translation2d(6.83, 2.83), 
                      new Translation2d(4 / 4, 4 / 4)//, 
                      //new Translation2d(1.17, 2.83)
                      ), 
              new Pose2d(0, 0, new Rotation2d(0)), 
              config);

    var thetaController =
        new ProfiledPIDController(
            AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    SwerveControllerCommand UmbrellaControllerCommand =
        new SwerveControllerCommand(
            Umbrella,
            m_robotDrive::getPoseMeters, // Functional interface to feed supplier
            DriveConstants.DRIVE_KINEMATICS,

            // Position controllers
            new PIDController(AutoConstants.kPXController, 0, 0),
            new PIDController(AutoConstants.kPYController, 0, 0),
            thetaController,
            m_robotDrive::setModuleStates,
            m_robotDrive);

            @SuppressWarnings("unused") // TODO: implement exampleControllerCommand
            SwerveControllerCommand exampleControllerCommand =
            new SwerveControllerCommand(
                exampleTrajectory,
                m_robotDrive::getPoseMeters, // Functional interface to feed supplier
                DriveConstants.DRIVE_KINEMATICS,
    
                // Position controllers
                new PIDController(AutoConstants.kPXController, 0, 0),
                new PIDController(AutoConstants.kPYController, 0, 0),
                thetaController,
                m_robotDrive::setModuleStates,
                m_robotDrive);

    // Reset odometry to the starting pose of the trajectory.
    m_robotDrive.resetOdometry(Umbrella.getInitialPose());

    // Run path following command, then stop at the end.
    return UmbrellaControllerCommand.andThen(() -> m_robotDrive.drive(0.0, 0.0, 0.0, true));
  }

  private static double deadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }

  private static double modifyAxis(double value) {
    // Deadband
    value = deadband(value, 0.05);

    // Square the axis
    value = Math.copySign(value * value, value);

    return value;
  }
}