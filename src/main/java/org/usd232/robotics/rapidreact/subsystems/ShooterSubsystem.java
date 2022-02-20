package org.usd232.robotics.rapidreact.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
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

    /** Turns on the shooter */
    public void shooterOn() {
        // Zeros the encoder
        shooterEncoder.setPosition(0);

        // Checks if the motor has rotated past the minRotationCount then moves to full speed
        shooter.setIdleMode(CANSparkMax.IdleMode.kCoast);
        if (Math.abs(shooterEncoder.getPosition()) <= ShooterConstants.MIN_ROTATION_COUNT) {
            shooter.set(0.5);

        } else if (Math.abs(shooterEncoder.getPosition()) >= ShooterConstants.MIN_ROTATION_COUNT) {
            shooter.set(1.0);
            
        } else {
            LOG.warn("shooterOn() broke. (How did you get here)");
        }

    }

    /** Turns off the shooter. Woah. */
    public void shooterOff() {
        shooter.set(0);
        shooterEncoder.setPosition(0);
    }

    /** Gets the position of the encoders. */
    public static double getEncoderPosition() {
        return shooterEncoder.getPosition();
    }
}
