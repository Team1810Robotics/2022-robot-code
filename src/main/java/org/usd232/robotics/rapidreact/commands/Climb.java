package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.ClimbSubsystem;

import static org.usd232.robotics.rapidreact.Constants.ClimbConstants.*;

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
        if (buttonPressed && ClimbSubsystem.endgame) {
            climbSubsystem.hooksUp();
        } else {
            if (climbSubsystem.getEncoderAve() >= WINCH_ENCODER_BOTTOM) {
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
