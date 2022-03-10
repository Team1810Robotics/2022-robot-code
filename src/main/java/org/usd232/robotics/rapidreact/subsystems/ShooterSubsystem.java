package org.usd232.robotics.rapidreact.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.usd232.robotics.rapidreact.log.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.usd232.robotics.rapidreact.Constants.ShooterConstants;

// https://drive.google.com/file/d/1Big3GqA8ZGPYKn6hLIk1PlT94j46iKOF/view?usp=sharing

public class ShooterSubsystem extends SubsystemBase {
    /**
     * The logger.
     * 
     * @since 2018
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();

    private static final CANSparkMax shooter = new CANSparkMax(ShooterConstants.MOTOR_PORT, MotorType.kBrushless);
    private static final RelativeEncoder shooterEncoder = shooter.getEncoder();
    private static final SparkMaxPIDController pidController = shooter.getPIDController();
    private static final double setPoint = ShooterConstants.MAX_HOLD_VELOCITY * 1.0;

    public ShooterSubsystem() {
        shooter.setInverted(true);
        shooter.setIdleMode(CANSparkMax.IdleMode.kCoast);
        pidController.setP(ShooterConstants.kP);
        pidController.setI(ShooterConstants.kI);
        pidController.setD(ShooterConstants.kD);
        pidController.setOutputRange(ShooterConstants.MIN_OUTUT, ShooterConstants.MAX_OUTPUT);
    }

    /** 
     * Turns on the shooter at max speed 
     * @param percent
     */
    public void shooterOn(double percent) {
        if (percent > 1) {
            percent = 1;
        } else if (percent < -1) {
            percent = -1;
        }
        shooter.set(percent); // 0.435 good for auto
    }


    /** Holds the shooter at its max speed with a PID controller. Doesn't work as of now. */
    public static void holdShooter() {
            pidController.setReference(setPoint, CANSparkMax.ControlType.kVelocity);
    }

    /** Holds the shooter at its minimum velocity when it's not being used. */
    public void manualHoldShooter() {
        if (getEncoderVelocity() <= ShooterConstants.MIN_VELOCITY) {
            this.shooterOn(1.0);
            LOG.info("Shooter On");
        } else if (getEncoderVelocity() >= ShooterConstants.MIN_VELOCITY + 100) {
            this.shooterOff();  // let it coast
            LOG.info("Shooter Off");
        }
    }

    /** Turns off the shooter. Woah. */
    public void shooterOff() {
        shooter.setIdleMode(CANSparkMax.IdleMode.kCoast);
        shooter.set(0);
        shooterEncoder.setPosition(0);
    }

    /** @return The position of the encoders. */
    public static double getEncoderPosition() {
        return shooterEncoder.getPosition();
    }

    /** @return The velocity of the encoders. */
    public static double getEncoderVelocity() {
        return shooterEncoder.getVelocity();
    }
}
