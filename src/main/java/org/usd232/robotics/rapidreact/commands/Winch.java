package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.ClimbSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Winch extends CommandBase {

    private final ClimbSubsystem climbSubsystem;

    public Winch(ClimbSubsystem climbSubsystem) {
        this.climbSubsystem = climbSubsystem;
    }

    @Override
    public void initialize() {
        climbSubsystem.winchOn();
    }

    @Override
    public void end(boolean inturrupted) {
        climbSubsystem.winchOff();
    }
}
