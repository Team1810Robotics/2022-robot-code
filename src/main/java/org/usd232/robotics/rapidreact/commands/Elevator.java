package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Elevator extends CommandBase {

    AugerSubsystem elevatorSubsystem;
    
    public Elevator(AugerSubsystem elevatorSubsystem) {
        this.elevatorSubsystem = elevatorSubsystem;
    }

    @Override
    public void execute() {
        elevatorSubsystem.elevatorOn();
    }

    @Override
    public void end(boolean inturrupted) {
        elevatorSubsystem.elevatorOff();
    }
}
