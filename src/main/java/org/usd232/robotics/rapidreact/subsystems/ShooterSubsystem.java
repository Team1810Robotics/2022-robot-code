package org.usd232.robotics.rapidreact.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.usd232.robotics.rapidreact.Constants.ShooterConstants;

public class ShooterSubsystem extends SubsystemBase {

    private static final CANSparkMax shooter = new CANSparkMax(ShooterConstants.MOTOR_PORT, MotorType.kBrushless);
    private static final RelativeEncoder shooterEncoder = shooter.getEncoder();

    private static final int minRotationCount = 10; // TODO: Find best value

    public void shooterOn() { // Needs testing
        shooter.setIdleMode(CANSparkMax.IdleMode.kCoast);
        if (shooterEncoder.getPosition() <= minRotationCount) {
            shooter.set(0.5);

        } else if (shooterEncoder.getPosition() >= minRotationCount) {
            shooter.set(1);
        }

    }

    public void shooterMotorOff() {
        shooter.set(0);
    }

    public void shooterOff() {
        // TODO: ASK IF THIS IS SAFE / EVEN NEEDED
        shooter.setIdleMode(CANSparkMax.IdleMode.kBrake);
        while (shooterEncoder.getVelocity() >= 3) {
            shooter.set(0);
        }
        shooter.setIdleMode(CANSparkMax.IdleMode.kCoast);
        shooterEncoder.setPosition(0);
    }

    public double getEncoderPosition() {
        return shooterEncoder.getPosition();
    }
}
