package org.usd232.robotics.rapidreact;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import org.usd232.robotics.rapidreact.commands.SwerveDrive;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import static org.usd232.robotics.rapidreact.Constants.ModuleConstants;

public class RobotContainer {
  private final DriveSubsystem m_drivetrainSubsystem = new DriveSubsystem();

  private final Joystick m_movJoystick = new Joystick(0);
  private final Joystick m_rotJoystick = new Joystick(1);
  //private final XboxController m_controller = new XboxController(0);

  public RobotContainer() {
    // Set up the default command for the drivetrain.
    // The controls are for field-oriented driving:
    // Left stick Y axis -> forward and backwards movement
    // Left stick X axis -> left and right movement
    // Right stick X axis -> rotation
    m_drivetrainSubsystem.setDefaultCommand(new SwerveDrive(
            m_drivetrainSubsystem,
            () -> -modifyAxis(m_movJoystick.getY()) * ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_movJoystick.getX()) * ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis((m_rotJoystick.getX() / 1.25)) * ModuleConstants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
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
    // Back button zeros the gyroscope
    new Button(m_movJoystick::getTrigger).whenPressed(m_drivetrainSubsystem::zeroGyroscope); // No requirements because we don't need to interrupt anything
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
    value = deadband(value, 0.25);

    // Square the axis
    value = Math.copySign(value * value, value);

    return value;
  }
}
