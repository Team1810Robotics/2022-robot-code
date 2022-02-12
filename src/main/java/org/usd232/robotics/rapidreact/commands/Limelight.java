package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Limelight extends CommandBase {

    private VisionSubsystem visionSubsystem;
    
    public Limelight(VisionSubsystem visionSubsystem) {
        this.visionSubsystem = visionSubsystem;
    }

    @Override
    public void initialize() {
        visionSubsystem.limeLightOn();
    }

    @Override
    public void end(boolean inturrupted) {
        visionSubsystem.limeLightOff();
    }
}
