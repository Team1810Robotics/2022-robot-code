package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.ClimbConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ClimbSubsystem {
    
    private static final Solenoid climbSolenoid = new Solenoid(PneumaticsModuleType.REVPH, ClimbConstants.PNEUMATIC_PORT);

    private static final CANSparkMax leftWinch = new CANSparkMax(ClimbConstants.LEFT_WINCH_PORT, MotorType.kBrushless);
    private static final CANSparkMax rightWinch = new CANSparkMax(ClimbConstants.RIGHT_WINCH_PORT, MotorType.kBrushless);
    private static final RelativeEncoder leftWinchEncoder = leftWinch.getEncoder();
    private static final RelativeEncoder rightWinchEncoder =  rightWinch.getEncoder();

    public static boolean endgame;

    public ClimbSubsystem() {
        leftWinch.setInverted(false);   // TODO
        rightWinch.setInverted(false);  // TODO
        leftWinch.setIdleMode(IdleMode.kBrake);
        rightWinch.setIdleMode(IdleMode.kBrake);

    }

    private static final RelativeEncoder leftWinchEncoder = leftWinch.getEncoder();
    private static final RelativeEncoder rightWinchEncoder =  rightWinch.getEncoder();

    public static final Timer timer = new Timer();

    public static boolean endgame;

    public ClimbSubsystem() {
        leftWinch.setInverted(false);   // TODO
        rightWinch.setInverted(false);  // TODO
        leftWinch.setIdleMode(IdleMode.kBrake);
        rightWinch.setIdleMode(IdleMode.kBrake);

    }


    /** Retracts the piston so the hooks go up */
    public void hooksUp() {
        climbSolenoid.set(true);
    }

    /** Resets the piston back to its original position. You probably won't need to use this. */
    public void resetPiston() {
        climbSolenoid.set(false);
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

    public double getLeftEncoder() {
        return leftWinchEncoder.getPosition();
    }

    public double getRightEncoder() {
        return rightWinchEncoder.getPosition();
    }

    public double getEncoderAve() {
        return (rightWinchEncoder.getPosition() + leftWinchEncoder.getPosition()) / 2;
    }

    public static void zeroEncoders() {
        leftWinchEncoder.setPosition(0);
        rightWinchEncoder.setPosition(0);
    }
}
