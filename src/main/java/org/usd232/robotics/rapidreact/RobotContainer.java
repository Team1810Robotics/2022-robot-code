package org.usd232.robotics.rapidreact;

import static org.usd232.robotics.rapidreact.Constants.OIConstants;

import org.usd232.robotics.rapidreact.commands.SwerveDrive;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
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
    return null;
  }
}
