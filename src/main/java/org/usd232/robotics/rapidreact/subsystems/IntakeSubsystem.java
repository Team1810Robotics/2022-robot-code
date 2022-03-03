package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.IntakeConstants;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Relay; // Motor controller is a spike
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    
    public static boolean sneezin;
    
    private static final Relay leftIntake = new Relay(IntakeConstants.LEFT_MOTOR_PORT, Relay.Direction.kBoth);
    private static final Relay rightIntake = new Relay(IntakeConstants.RIGHT_MOTOR_PORT, Relay.Direction.kBoth);

    private static final Solenoid leftSolenoid = new Solenoid(PneumaticsModuleType.REVPH, IntakeConstants.LEFT_PNEUMATIC_PORT);
    private static final Solenoid rightSolenoid = new Solenoid(PneumaticsModuleType.REVPH, IntakeConstants.RIGHT_PNEUMATIC_PORT);

    /** Lowers the left intake for collection and turns it on */
    public void collectLeft() {
        leftSolenoid.set(true);
        leftIntake.set(Relay.Value.kForward);
    }
    /** Lowers the right intake for collection and turns it on */
    public void collectRight() {
        rightSolenoid.set(true);
        rightIntake.set(Relay.Value.kForward);
    }

    /** Raises and turns off the left intake */
    public void returnLeft() {
        leftSolenoid.set(false);
        leftIntake.set(Relay.Value.kOff);
    }

    /** Raises and turns off the right intake */
    public void returnRight() {
        rightSolenoid.set(false);
        rightIntake.set(Relay.Value.kOff);
    }

    public void sneeze() {
        sneezin = true;
    }
}
