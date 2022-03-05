package org.usd232.robotics.rapidreact.commands.testCommands;

import org.usd232.robotics.rapidreact.subsystems.EjectorSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Eject extends CommandBase {

    private final EjectorSubsystem ejectorSubsystem;

    public Eject(EjectorSubsystem ejectorSubsystem) {
        this.ejectorSubsystem = ejectorSubsystem;
    }

    @Override
    public void execute() {
        ejectorSubsystem.eject();
    }

    @Override 
    public void end(boolean inturrupted) {
        ejectorSubsystem.resetEjecter();
    }
    
}
