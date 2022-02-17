package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.ClimbConstants;

import com.revrobotics.CANSparkMax;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;

public class ClimbSubsystem {
    
    private static final Servo climbServo = new Servo(ClimbConstants.SERVO_MOTOR_CHANNEL);

    private static final CANSparkMax leftWinch = new CANSparkMax(ClimbConstants.LEFT_WINCH_PORT, MotorType.kBrushless);
    private static final CANSparkMax rightWinch = new CANSparkMax(ClimbConstants.RIGHT_WINCH_PORT, MotorType.kBrushless);

    /**
     * Set the servo position.
     *
     * <p>Servo values range from 0.0 to 1.0 corresponding to the range of full left to full right.
     *
     * @param value Position from 0.0 to 1.0.
     */
    public void setServo(int value) {
        climbServo.set(value);
    }

    /** Turns both right and left Winch motors on */
    public void winchOn() {
        leftWinch.set(0.5);     // TODO: Test Speed
        rightWinch.set(0.5);    // TODO: Test Speed
    }

    /** stops all winch movment */
    public void winchOff() {
        leftWinch.set(0.0);
        rightWinch.set(0.0);
    }
}
