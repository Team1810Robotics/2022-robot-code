package org.usd232.robotics.rapidreact.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.usd232.robotics.rapidreact.log.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.usd232.robotics.rapidreact.Constants.ShooterConstants;

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

    private static final int minRotationCount = 10; // TODO: Find best value

    public void shooterOn() {
        // Zeros the encoder
        shooterEncoder.setPosition(0);

        // Checks if the motor has rotated past the minRotationCount then moves to full speed
        shooter.setIdleMode(CANSparkMax.IdleMode.kCoast);
        if (shooterEncoder.getPosition() <= minRotationCount) {
            shooter.set(0.5);

        } else if (shooterEncoder.getPosition() >= minRotationCount) {
            shooter.set(1.0);
        } else {
            LOG.warn("Shooter On broke. (How did you get here)");
        }

    }

    public void shooterOff() {
        shooter.set(0);
    }

    public double getEncoderPosition() {
        return shooterEncoder.getPosition();
    }
}
