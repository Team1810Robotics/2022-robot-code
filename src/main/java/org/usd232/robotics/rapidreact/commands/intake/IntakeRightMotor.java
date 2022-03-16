package org.usd232.robotics.rapidreact.commands.intake;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

// Part 3: Most Jank code ive ever written
public class IntakeRightMotor extends CommandBase {
    
    private final IntakeSubsystem intakeSubsystem;
    private final XboxController xbox;
    
    public IntakeRightMotor(IntakeSubsystem intakeSubsystem , XboxController xbox) {

        this.intakeSubsystem = intakeSubsystem;
        this.xbox = xbox;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.rightMotor(!xbox.getBackButton());
    }
    @Override
    public void end(boolean inturrupted) {
        intakeSubsystem.motorOff(true);
    }
}