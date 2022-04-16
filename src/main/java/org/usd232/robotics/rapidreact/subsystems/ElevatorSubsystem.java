package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.ElevatorConstants;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {
    
    private final static Relay elevator = new Relay(ElevatorConstants.PORT, Relay.Direction.kForward); // FIXME kForward (if needed)

    /** Turns the elevotor on */
    public void elevatorOn() {
        elevator.set(Relay.Value.kOn);
    }

    /** Turns the elevotor off */
    public void elevatorOff() {
        elevator.set(Relay.Value.kOff);
    }
}
