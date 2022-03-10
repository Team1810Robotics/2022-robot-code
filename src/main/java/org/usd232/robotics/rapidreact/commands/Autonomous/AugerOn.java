package org.usd232.robotics.rapidreact.commands.autonomous;

import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class AugerOn extends CommandBase {

    private final AugerSubsystem augerSubsystem;
    private final boolean on;
    
    public AugerOn(AugerSubsystem augerSubsystem, boolean on) {
        this.augerSubsystem = augerSubsystem;
        this.on = on;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        augerSubsystem.elevatorOn(true);
    }

    @Override 
    public boolean isFinished() {
        return !on;
    }

    @Override
    public void end(boolean interrupted) {
        augerSubsystem.elevatorOff();
    }
}