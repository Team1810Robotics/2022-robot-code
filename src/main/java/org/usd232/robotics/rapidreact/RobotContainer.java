package org.usd232.robotics.rapidreact;

import static org.usd232.robotics.rapidreact.Constants.*;
import static org.usd232.robotics.rapidreact.IO.*;
import org.usd232.robotics.rapidreact.log.Logger;

/* Commands */
import org.usd232.robotics.rapidreact.commands.Auger;
import org.usd232.robotics.rapidreact.commands.Hood;
import org.usd232.robotics.rapidreact.commands.Intake;
import org.usd232.robotics.rapidreact.commands.Limelight;
import org.usd232.robotics.rapidreact.commands.SwerveDrive;
/* end of Commands */

/* Paths */
import org.usd232.robotics.rapidreact.commands.autonomous.paths.OffLine2;
import org.usd232.robotics.rapidreact.commands.autonomous.paths.OffLineLeft;
import org.usd232.robotics.rapidreact.commands.autonomous.paths.OffLineRight;
import org.usd232.robotics.rapidreact.commands.autonomous.paths.OffLineSpin;
import org.usd232.robotics.rapidreact.commands.autonomous.paths.OfflineReversed;
import org.usd232.robotics.rapidreact.commands.autonomous.paths.Shoot;
/* End of Paths */

/* Subsystems */
import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;
import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;
import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;
import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;
/* End of Subsystems */

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

// https://drive.google.com/file/d/1EBKde_UrpQlax-PRKJ1Qa8nDJuIpd07K/view?usp=sharing

public class RobotContainer {
    /**
     * The logger.
     * 
     * @since 2018
     */
    private static final Logger LOG = new Logger();

    SendableChooser<Command> pathChooser = new SendableChooser<>();

    private final AugerSubsystem m_augerSubsystem = LOG.catchAll(() -> new AugerSubsystem());
    private final DriveSubsystem m_driveSubsystem = LOG.catchAll(() -> new DriveSubsystem());
    private final IntakeSubsystem m_intakeSubsystem = LOG.catchAll(() -> new IntakeSubsystem());
    private final ShooterSubsystem m_shooterSubsystem = LOG.catchAll(() -> new ShooterSubsystem());
    private final HoodSubsystem m_hoodSubsystem = LOG.catchAll(() -> new HoodSubsystem());
    private final VisionSubsystem m_visionSubsystem = LOG.catchAll(() -> new VisionSubsystem());

    /* Auto Path(s) */
    private final Command m_offLineReversed = LOG.catchAll(() -> new OfflineReversed(m_driveSubsystem, m_shooterSubsystem, m_augerSubsystem));
    private final Command m_offLineLeft = LOG.catchAll(() -> new OffLineLeft(m_driveSubsystem, m_shooterSubsystem, m_augerSubsystem));
    private final Command m_offLineRight = LOG.catchAll(() -> new OffLineRight(m_driveSubsystem, m_shooterSubsystem, m_augerSubsystem));
    private final Command m_offLineTwo = LOG.catchAll(() -> new OffLine2(m_driveSubsystem, m_shooterSubsystem, m_augerSubsystem));
    private final Command m_shoot = LOG.catchAll(() -> new Shoot(m_shooterSubsystem, m_augerSubsystem));
    private final Command m_offLineSpin = LOG.catchAll(() -> new OffLineSpin(m_driveSubsystem, m_shooterSubsystem, m_augerSubsystem));

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
                () -> true
        ));

        /* Path chooser */
        pathChooser.setDefaultOption("Null", null);
        pathChooser.addOption("OffLine Left", m_offLineLeft);
        pathChooser.addOption("OffLine Right", m_offLineRight);
        pathChooser.addOption("-Shoot & Offline Reversed-", m_offLineReversed);
        pathChooser.addOption("Off Line 2", m_offLineTwo);
        pathChooser.addOption("Shoot", m_shoot);
        pathChooser.addOption("Off Line Spin", m_offLineSpin);
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
        rotationJoystick_Button9.whenPressed(() -> m_driveSubsystem.zeroGyroscope());
        movementJoystick_Button9.whenPressed(() -> m_driveSubsystem.zeroGyroscope());

        ManipulatorXbox_B.whenHeld(new Auger(m_augerSubsystem, manipulatorController), false);

        ManipulatorXbox_X.toggleWhenActive(new Limelight(m_visionSubsystem, m_hoodSubsystem), true);

        ManipulatorXbox_RB.whenHeld(new Intake(m_intakeSubsystem, manipulatorController, true));
        ManipulatorXbox_LB.whenHeld(new Intake(m_intakeSubsystem, manipulatorController, false));

        ManipulatorXbox_LStick.whenHeld(new Hood(m_hoodSubsystem, true));
        ManipulatorXbox_RStick.whenHeld(new Hood(m_hoodSubsystem, false));

        ManipulatorXbox_Y.whenPressed(() -> m_hoodSubsystem.resetHood());
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return pathChooser.getSelected();
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
        value = deadband(value, OIConstants.DEADBAND);

        // Square the axis
        value = Math.copySign(value * value, value);

        return value;
    }
}