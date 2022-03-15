package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class IntakeMotor extends CommandBase {
    
    private final IntakeSubsystem intakeSubsystem;
    private final XboxController xbox;
    private final boolean right;
    
    public IntakeMotor(IntakeSubsystem intakeSubsystem, XboxController xbox, boolean right) {

        this.intakeSubsystem = intakeSubsystem;
        this.xbox = xbox;
        this.right = right;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() { /* I Was Here, so was I */ }

    @Override
    public void execute() {
        if (right) {
            intakeSubsystem.rightMotor(!xbox.getBackButton());
        } else {
            intakeSubsystem.leftMotor(!xbox.getBackButton());
        }
    }

    @Override
    public void end(boolean inturrupted) {
        intakeSubsystem.motorOff(right);
    }
}
