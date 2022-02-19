package org.usd232.robotics.rapidreact.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import static org.usd232.robotics.rapidreact.Constants.EjectorConstants;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class EjectorSubsystem extends SubsystemBase {
    
    private static final Solenoid ballEjector = new Solenoid(PneumaticsModuleType.REVPH, EjectorConstants.PNEUMATIC_PORT);

    /**  */
    public void eject() {
        ballEjector.set(true);
        new WaitCommand(1.0); // TODO: Test
        ballEjector.set(false);
    }
}