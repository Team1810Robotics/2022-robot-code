package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class LimelightOn extends CommandBase {
    
    public LimelightOn() {}

    @Override
    public void initialize() {
        VisionSubsystem.limeLightOn();
    }

    @Override
    public void end(boolean inturrupted) {
        VisionSubsystem.limeLightOff();
    }
}
