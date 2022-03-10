package org.usd232.robotics.rapidreact.commands.autonomous;

import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShooterOn extends CommandBase {

    private final ShooterSubsystem shooterSubsystem;
    private final double percent;
    private final boolean on;
    
    public ShooterOn(ShooterSubsystem shooterSubsystem, double percent, boolean on) {
        this.shooterSubsystem = shooterSubsystem;
        this.percent = percent;
        this.on = on;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        shooterSubsystem.shooterOn(percent);
    }

    @Override 
    public boolean isFinished() {
        return !on;
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.shooterOff();
    }
}
