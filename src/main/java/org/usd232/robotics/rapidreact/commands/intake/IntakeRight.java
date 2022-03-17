package org.usd232.robotics.rapidreact.commands.intake;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class IntakeRight extends CommandBase {

    private final IntakeSubsystem intakeSubsystem;
    private final XboxController xbox;
    
    public IntakeRight(IntakeSubsystem intakeSubsystem, XboxController xbox) {

        this.intakeSubsystem = intakeSubsystem;
        this.xbox = xbox;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.rightPneumatic(true);
        intakeSubsystem.rightMotor(!xbox.getBackButton());
    }

    @Override
    public boolean isFinished() {
        if (!xbox.getRightBumper()) {
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.rightPneumatic(false);
        intakeSubsystem.motorOff(true);    // Turns off the right motor(s). . .
    }
}