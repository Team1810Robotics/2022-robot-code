package org.usd232.robotics.rapidreact.commands.intake;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Part 2: Most Jank code ive ever written
public class IntakeLeftPneumatic extends CommandBase {
    
    private final IntakeSubsystem intakeSubsystem;
    
    public IntakeLeftPneumatic(IntakeSubsystem intakeSubsystem) {

        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.leftPneumatic(true);
    }

    @Override
    public void end(boolean inturrupted) {
        intakeSubsystem.leftPneumatic(false);
    }
}
