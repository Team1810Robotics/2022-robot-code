package org.usd232.robotics.rapidreact.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.usd232.robotics.rapidreact.Constants.ShooterConstants;

// https://drive.google.com/file/d/1Big3GqA8ZGPYKn6hLIk1PlT94j46iKOF/view?usp=sharing

public class ShooterSubsystem extends SubsystemBase {

    private static final CANSparkMax shooter = new CANSparkMax(ShooterConstants.MOTOR_PORT, MotorType.kBrushless);
    private static final RelativeEncoder shooterEncoder = shooter.getEncoder();
    private static final SparkMaxPIDController pidController = shooter.getPIDController();
    private static final double setPoint = ShooterConstants.MAX_VELOCITY_RPM * 0.9; // Runs at 90% speed
    private static boolean on = false; // Perma jank

    public ShooterSubsystem() {
        shooter.setInverted(true);
        pidController.setP(ShooterConstants.kP);
        pidController.setI(ShooterConstants.kI);
        pidController.setD(ShooterConstants.kD);
        pidController.setOutputRange(ShooterConstants.MIN_OUTUT, ShooterConstants.MAX_OUTPUT);
    }

    /** Turns on the shooter */
    public void shooterOn() {
        shooter.set(0.9);
        on = true;
    }

    public void holdShooterVelocity() {
        if (on) {
            pidController.setReference(setPoint, CANSparkMax.ControlType.kVelocity);
        } else {
            this.shooterOn();
        }
    }

    /** Turns off the shooter. Woah. */
    public void shooterOff() {
        shooter.set(0);
        shooterEncoder.setPosition(0);
        on = false;
    }

    /** Gets the position of the encoders. */
    public static double getEncoderPosition() {
        return shooterEncoder.getPosition();
    }
}
