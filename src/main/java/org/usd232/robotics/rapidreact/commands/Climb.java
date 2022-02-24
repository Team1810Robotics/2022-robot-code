package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.ClimbSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** 
Goal: 
    if (endgame) {
        win
    }
*/
public class Climb extends CommandBase {

    private final ClimbSubsystem climbSubsystem;
    private final boolean buttonPressed;

    public Climb(ClimbSubsystem climbSubsystem, boolean pressed) {
        this.climbSubsystem = climbSubsystem;
        this.buttonPressed = pressed;
    }

    @Override
    public void execute() {
        if (buttonPressed) {
            climbSubsystem.hooksUp();
        } else {
            if (climbSubsystem.getEncoderAve() >= 1) {
                climbSubsystem.winchOn();
            } else {
                climbSubsystem.winchOff();
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        climbSubsystem.winchOff();
    }
}