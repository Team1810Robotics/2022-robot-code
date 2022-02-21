package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.AugerConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AugerSubsystem extends SubsystemBase { // Motor controller test value: 1255
    
    private final static VictorSPX auger = new VictorSPX(AugerConstants.ID);

    /** Turns the elevotor on */
    public void elevatorOn() {
        auger.set(ControlMode.PercentOutput, 0.30); // TODO: Test Value
    }

    public void elevatorReverse() {
        auger.set(ControlMode.PercentOutput, -0.50);
    }

    /** Turns the elevotor off */
    public void elevatorOff() {
        auger.set(ControlMode.PercentOutput, 0.0);
    }
}
