package org.usd232.robotics.rapidreact.commands.intake;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

// Part 4: Most Jank code ive ever written
public class IntakeRightPneumatic extends CommandBase {
    
    private final IntakeSubsystem intakeSubsystem;
    
    public IntakeRightPneumatic(IntakeSubsystem intakeSubsystem) {

        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.rightPneumatic(true);
    }

    @Override
    public void end(boolean inturrupted) {
        intakeSubsystem.rightPneumatic(false);
    }
}
