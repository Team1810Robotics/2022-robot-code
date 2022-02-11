package org.usd232.robotics.rapidreact.subsystems;

import static org.usd232.robotics.rapidreact.Constants.CompressorConstants;
import static org.usd232.robotics.rapidreact.Constants.IntakeConstants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Relay; // Motor controller is a spike
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    
    public static final Compressor compressor = new Compressor(CompressorConstants.COMPRESSOR_MODULE_ID, PneumaticsModuleType.REVPH);
    
    private static final Relay leftIntake = new Relay(IntakeConstants.LEFT_MOTOR_PORT, Relay.Direction.kForward);
    private static final Relay rightIntake = new Relay(IntakeConstants.RIGHT_MOTOR_PORT, Relay.Direction.kForward);

    private static final Solenoid leftSolenoid = new Solenoid(PneumaticsModuleType.REVPH, IntakeConstants.LEFT_PNEUMATIC_PORT);
    private static final Solenoid rightSolenoid = new Solenoid(PneumaticsModuleType.REVPH, IntakeConstants.RIGHT_PNEUMATIC_PORT);

    public IntakeSubsystem() {
        compressor.enableHybrid(IntakeConstants.MIN_TANK_PSI, IntakeConstants.MAX_TANK_PSI);
    }

    public void collectLeft() {
        leftSolenoid.set(true);
        leftIntake.set(Relay.Value.kOn);
    }

    public void collectRight() {
        rightSolenoid.set(true);
        rightIntake.set(Relay.Value.kOn);
    }

    public void returnLeft() {
        leftSolenoid.set(false);
        leftIntake.set(Relay.Value.kOff);
    }

    public void returnRight() {
        rightSolenoid.set(false);
        rightIntake.set(Relay.Value.kOff);
    }

    /******************************** FOR TESTING! ********************************/

    public void moveRightSolenoid() {
        rightSolenoid.set(true);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
        rightSolenoid.set(false);
    }

    public void moveLeftSolenoid() {
        leftSolenoid.set(true);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {}
        leftSolenoid.set(false);
    }
}
