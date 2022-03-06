package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** Just a test command to have a visual cue if the controller binding is working */
public class LimelightOn extends CommandBase {

    private final VisionSubsystem visionSubsystem;
    
    public LimelightOn(VisionSubsystem visionSubsystem) {
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