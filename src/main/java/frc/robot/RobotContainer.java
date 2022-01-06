package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.log.Logger;
import frc.robot.subsystems.DriveSubsystem;

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
    // An ExampleCommand will run in autonomous
    return new InstantCommand();
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