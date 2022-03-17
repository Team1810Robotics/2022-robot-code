package org.usd232.robotics.rapidreact.commands.intake;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class IntakeLeft extends CommandBase {

    private final IntakeSubsystem intakeSubsystem;
    private final XboxController xbox;
    
    public IntakeLeft(IntakeSubsystem intakeSubsystem, XboxController xbox) {

        this.intakeSubsystem = intakeSubsystem;
        this.xbox = xbox;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.leftPneumatic(true);
        intakeSubsystem.leftMotor(!xbox.getBackButton());
    }

    @Override
    public boolean isFinished() {
        if (!xbox.getLeftBumper()) {
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.leftPneumatic(false);
        intakeSubsystem.motorOff(false);    // Turns off the left motor(s). . .
    }
}