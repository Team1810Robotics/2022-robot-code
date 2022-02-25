package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Shooter extends CommandBase{

    ShooterSubsystem shooterSubsystem;
    
    public Shooter(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(shooterSubsystem);
    }


    @Override
    public void execute() {
        shooterSubsystem.shooterOn();
    }

    @Override
    public void end(boolean inturrupted) {
        shooterSubsystem.shooterOff();
    }
}
