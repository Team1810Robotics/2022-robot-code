package org.usd232.robotics.rapidreact;

import static org.usd232.robotics.rapidreact.Constants.PneumaticConstants;

import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.EjectorSubsystem;
import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;
//import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;

import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The first class that is called to set everything up
 * 
 * @author Noah, Josh, Jack W;
 * @since 2022
 */
public class Robot extends TimedRobot {

    private PneumaticHub m_ph = new PneumaticHub(PneumaticConstants.PH_CAN_ID);

    private Command m_autonomousCommand;

    private RobotContainer m_robotContainer;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        // Turns Limelight off on startup
        VisionSubsystem.limeLightOff();

        // Resets the hood on startup (could be annoying during testing)
        // HoodSubsystem.resetHood(); // TODO: notice me 

        // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {

        // read values periodically
        double gyroAngle = DriveSubsystem.getGyro();
        boolean gyroZero = DriveSubsystem.ifGyroZero();

        // post to smart dashboard periodically
        SmartDashboard.putNumber("Gyroscope angle", gyroAngle);
        SmartDashboard.putBoolean("Gyro 0", gyroZero);
        SmartDashboard.putBoolean("Lime Light On/Off", VisionSubsystem.OnOffLL);
        EjectorSubsystem.colorDebug();

        /**
         * Enable the compressor with hybrid sensor control, meaning it uses both
         * the analog and digital pressure sensors.
         *
         * This uses hysteresis between a minimum and maximum pressure value,
         * the compressor will run when the sensor reads below the minimum pressure
         * value, and the compressor will shut off once it reaches the maximum.
         *
         * If at any point the digital pressure switch is open, the compressor will
         * shut off.
         */
        m_ph.enableCompressorHybrid(PneumaticConstants.MIN_TANK_PSI, PneumaticConstants.MAX_TANK_PSI);

        CommandScheduler.getInstance().run();
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {
        // Turns Limelight off on disable
        VisionSubsystem.limeLightOff();
    }

    @Override
    public void disabledPeriodic() {}

    /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {}

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}
}
