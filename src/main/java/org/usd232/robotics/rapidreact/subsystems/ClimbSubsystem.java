package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.ClimbConstants;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

import static com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ClimbSubsystem {
    
    private static final Solenoid climbSolenoid = new Solenoid(PneumaticsModuleType.REVPH, ClimbConstants.PNEUMATIC_PORT);

    private static final CANSparkMax leftWinch = new CANSparkMax(ClimbConstants.LEFT_WINCH_PORT, MotorType.kBrushless);
    private static final CANSparkMax rightWinch = new CANSparkMax(ClimbConstants.RIGHT_WINCH_PORT, MotorType.kBrushless);

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
}
