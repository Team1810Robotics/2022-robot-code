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
    private static final double setPoint = ShooterConstants.MAX_VELOCITY * 1.0;
    private static boolean on = false; // Perm jank

    public ShooterSubsystem() {
        shooter.setInverted(true);
        shooter.setIdleMode(CANSparkMax.IdleMode.kCoast);
        pidController.setP(ShooterConstants.kP);
        pidController.setI(ShooterConstants.kI);
        pidController.setD(ShooterConstants.kD);
        pidController.setOutputRange(ShooterConstants.MIN_OUTUT, ShooterConstants.MAX_OUTPUT);
    }

    /** Turns on the shooter */
    public void shooterOn() {
        shooter.set(1);
        on = true;
    }

    /** Turns the shooter on at desired speed (between 1 & -1) */
    public void shooterOn(double speed) {
        if (speed > 1) {
            speed = 1;
        } else if (speed < -1) {
            speed = -1;
        }
        shooter.set(speed);
    }

    public static void holdShooter() {
        if (on) {
            pidController.setReference(setPoint, CANSparkMax.ControlType.kVelocity);
        }
    }

    public void manualHoldShooter() {
        if (getEncoderVelocity() <= ShooterConstants.MIN_VELOCITY) {
            this.shooterOn(1.0);
        } else {
            this.shooterOff();  // let it coast
        }
    }

    /** Turns off the shooter. Woah. */
    public void shooterOff() {
        shooter.setIdleMode(CANSparkMax.IdleMode.kCoast);
        shooter.set(0);
        shooterEncoder.setPosition(0);
        on = false;
    }

    /** Gets the position of the encoders. */
    public static double getEncoderPosition() {
        return shooterEncoder.getPosition();
    }

    public static double getEncoderVelocity() {
        return shooterEncoder.getVelocity();
    }
}
