package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Intake extends CommandBase {

    IntakeSubsystem intakeSubsystem;
    boolean right;
    
    public Intake(IntakeSubsystem intakeSubsystem, boolean right) {
        this.intakeSubsystem = intakeSubsystem;
        this.right = right;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() { /* I Was Here */ }

    @Override
    public void execute() {
        if (right) {
            intakeSubsystem.collectRight();
        } else {
            intakeSubsystem.collectLeft();
        }
    }

    @Override
    public void end(boolean inturrupted) {
        if (right) {
            intakeSubsystem.returnRight();
        } else {
            intakeSubsystem.returnLeft();
        }
    }
}
