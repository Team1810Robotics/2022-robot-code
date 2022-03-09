package org.usd232.robotics.rapidreact;

import static org.usd232.robotics.rapidreact.Constants.PneumaticConstants;

import org.usd232.robotics.rapidreact.commands.HoodTarget;
import org.usd232.robotics.rapidreact.log.Logger;
/* Subsystems */
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.EjectorSubsystem;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;
import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;
import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;
/* End of Subsystems */

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
    /**
     * The logger.
     * 
     * @since 2018
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();

    private PneumaticHub m_ph = new PneumaticHub(PneumaticConstants.PH_CAN_ID);

    private Command m_autonomousCommand;

    private RobotContainer m_robotContainer;

    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    private final VisionSubsystem m_visionSubsystem = new VisionSubsystem();
    private final HoodSubsystem m_hoodSubsystem = new HoodSubsystem();

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
        // Turns shooter on (Wow).
        // shooterSubsystem.shooterOn();
        
        // Turns Limelight off on startup
        m_visionSubsystem.limeLightOff();
        
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
        
        // post to smart dashboard periodically
        SmartDashboard.putNumber("Gyroscope angle", DriveSubsystem.getGyro());
        SmartDashboard.putBoolean("Gyro 0", DriveSubsystem.ifGyroZero());
        SmartDashboard.putBoolean("Lime Light On/Off", VisionSubsystem.OnOffLL);
        SmartDashboard.putNumber("Hood Encoder", HoodSubsystem.hoodEncoder.getDistance());
        SmartDashboard.putBoolean("Hood LS", HoodSubsystem.hoodLS.get());
        SmartDashboard.putNumber("Compressor PSI", m_ph.getPressure(0));
        SmartDashboard.putNumber("Shooter Speed", ShooterSubsystem.getEncoderVelocity());
        SmartDashboard.putBoolean("Manual Shooting", ShooterSubsystem.manualShooting);
        SmartDashboard.putNumber("Calculated Target Distance", m_visionSubsystem.getTargetDistance());
        EjectorSubsystem.colorDebug();
        
        /** Enable compressor closed loop control using analog input. */
        m_ph.enableCompressorAnalog(PneumaticConstants.MIN_TANK_PSI, PneumaticConstants.MAX_TANK_PSI);
        

        // Should apply the value given in ShuffleBoard to the hood
        new HoodTarget(m_hoodSubsystem, SmartDashboard.getNumber("Hood Encoder Control", 0)); // TODO: Test

        // Manual way to hold speed (maybe.)
        // https://drive.google.com/file/d/1Mhkmg6CINcqzfc9p3ie6-v02ha7aCzSx/view?usp=sharing
        if(!ShooterSubsystem.manualShooting){
            shooterSubsystem.manualHoldShooter();   // TODO: Test
        }

        new Thread(() -> {
            try {
                LOG.info("Moved to a new thread");
                VisionSubsystem.hoodDistance = 0;   // Put formula here
                
            } catch (Exception e) {
                LOG.error("Failed to move to a new thread");
            }
        }).start();
        
        CommandScheduler.getInstance().run();
    }
    
    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {
        // Turns Limelight off on disable
        m_visionSubsystem.limeLightOff();
        
        // Turn off shooter motor
        shooterSubsystem.shooterOff();
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
    public void teleopPeriodic() {
        
    }
}
