package org.usd232.robotics.rapidreact;

import static org.usd232.robotics.rapidreact.Constants.ModuleConstants;
import static org.usd232.robotics.rapidreact.Constants.OIConstants;
import static org.usd232.robotics.rapidreact.commands.XboxTrigger.Hand;

import org.usd232.robotics.rapidreact.commands.SwerveDrive;
import org.usd232.robotics.rapidreact.commands.XboxTrigger;
import org.usd232.robotics.rapidreact.commands.Autonomous.DriveDistance;
import org.usd232.robotics.rapidreact.commands.Autonomous.Paths.BlueLeftQuad;
import org.usd232.robotics.rapidreact.commands.Autonomous.Paths.BlueRightQuad;
import org.usd232.robotics.rapidreact.commands.Autonomous.Paths.RedLeftQuad;
import org.usd232.robotics.rapidreact.commands.Autonomous.Paths.RedRightQuad;
import org.usd232.robotics.rapidreact.log.Logger;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {
    /**
     * The logger.
     * 
     * @since 2018
     */
    private static final Logger LOG = new Logger();

    SendableChooser<Command> pathChooser = new SendableChooser<>();

    private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();

    // Contollers
    private final Joystick movementJoystick = LOG.catchAll(() -> new Joystick(OIConstants.MOVEMENT_JOYSTICK_PORT));
    private final Joystick rotationJoystick = LOG.catchAll(() -> new Joystick(OIConstants.ROTATION_JOYSTICK_PORT));
    private final XboxController manipulatorController = LOG.catchAll(() ->new XboxController(OIConstants.MANIPULATOR_CONTROLLER_PORT));

    // Xbox buttons
    private final XboxTrigger ManipulatorXbox_TriggerL = LOG.catchAll(() -> new XboxTrigger(manipulatorController, Hand.kLeft));
    // private final XboxTrigger ManipulatorXbox_TriggerR = LOG.catchAll(() -> new XboxTrigger(manipulatorController, XboxTrigger.Hand.kRight));
    private final JoystickButton ManipulatorXbox_A = LOG.catchAll(() -> new JoystickButton(manipulatorController, 1));
    // private final JoystickButton ManipulatorXbox_B = LOG.catchAll(() -> new JoystickButton(manipulatorController, 2));
    // private final JoystickButton ManipulatorXbox_X = LOG.catchAll(() -> new JoystickButton(manipulatorController, 3));
    // private final JoystickButton ManipulatorXbox_Y = LOG.catchAll(() -> new JoystickButton(manipulatorController, 4));
    // private final JoystickButton ManipulatorXbox_LB = LOG.catchAll(() -> new JoystickButton(manipulatorController, 5));
    // private final JoystickButton ManipulatorXbox_RB = LOG.catchAll(() -> new JoystickButton(manipulatorController, 6));
    // private final JoystickButton ManipulatorXbox_Back = LOG.catchAll(() -> new JoystickButton(manipulatorController, 7));
    // private final JoystickButton ManipulatorXbox_Start = LOG.catchAll(() -> new JoystickButton(manipulatorController, 8));
    // private final JoystickButton ManipulatorXbox_LStick = LOG.catchAll(() -> new JoystickButton(manipulatorController, 9));
    // private final JoystickButton ManipulatorXbox_RStick = LOG.catchAll(() -> new JoystickButton(manipulatorController, 10));

    /* Auto Paths */
    private final Command m_driveDistance = LOG.catchAll(() -> new DriveDistance(m_driveSubsystem, -5, 0));
    private final Command m_blueLeft = LOG.catchAll(() -> new BlueLeftQuad(m_driveSubsystem));
    private final Command m_blueRight = LOG.catchAll(() -> new BlueRightQuad(m_driveSubsystem));
    private final Command m_redLeft = LOG.catchAll(() -> new RedLeftQuad(m_driveSubsystem));
    private final Command m_redRight = LOG.catchAll(() -> new RedRightQuad(m_driveSubsystem));

    private static final double DEADBAND = 0.25;

    /** Turns joystick inputs into speed variables */
    public RobotContainer() {
        // Set up the default command for the drive.
        // Left stick Y axis -> forward and backwards movement
        // Left stick X axis -> left and right movement
        // Right stick X axis -> rotation
        // Bool for field-oriented driving (true for field-oriented driving)
        m_driveSubsystem.setDefaultCommand(new SwerveDrive(
            m_driveSubsystem,
                () -> -modifyAxis(movementJoystick.getY()) * ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND,
                () -> -modifyAxis(movementJoystick.getX()) * ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND,
                () -> -modifyAxis((rotationJoystick.getX() / 1.25)) * ModuleConstants.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND,
                true
        ));

        /* Path chooser */
        pathChooser.setDefaultOption("Drive Back", m_driveDistance);
        pathChooser.addOption("Left Blue Tarmac", m_blueLeft);
        pathChooser.addOption("Right Blue Tarmac", m_blueRight);
        pathChooser.addOption("Left Blue Tarmac", m_redLeft);
        pathChooser.addOption("Rightt Blue Tarmac", m_redRight);
        Shuffleboard.getTab("Autonomous").add(pathChooser);

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
        new Button(movementJoystick::getTrigger).whenPressed(m_driveSubsystem::zeroGyroscope); // No requirements because we don't need to interrupt anything
        new Button(rotationJoystick::getTrigger).whenPressed(m_driveSubsystem::zeroGyroscope);

        ManipulatorXbox_TriggerL.whenActive(m_driveSubsystem::zeroGyroscope);
        ManipulatorXbox_A.whenActive(m_driveSubsystem::zeroGyroscope);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return null;
    }

    /** Sets the deadzone for the controller/joystick */
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

    /** Applies deadband and Copies the sign */
    private static double modifyAxis(double value) {
        // Deadband
        value = deadband(value, DEADBAND);

        // Square the axis
        value = Math.copySign(value * value, value);

        return value;
    }
}
