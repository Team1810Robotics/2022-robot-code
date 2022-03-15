package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Intake extends CommandBase {

    private final IntakeSubsystem intakeSubsystem;
    private final XboxController xbox;
    private final boolean motor;
    private final boolean right;
    
    public Intake(IntakeSubsystem intakeSubsystem , XboxController xbox, boolean motor, boolean right) {

        this.intakeSubsystem = intakeSubsystem;
        this.xbox = xbox;
        this.motor = motor;
        this.right = right;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() { /* I Was Here, so was I */ }

    @Override
    public void execute() {
        if (right && !motor) {
            intakeSubsystem.rightPneumatic(true);
        } else if (!right && !motor) {
            intakeSubsystem.leftPneumatic(true);
        }
        
        if (right && motor) {
            intakeSubsystem.rightMotor(!xbox.getBackButton());
        } else if (!right && motor) {
            intakeSubsystem.leftMotor(!xbox.getBackButton());
        }
    }

    @Override
    public void end(boolean inturrupted) {
        if (right && !motor) {
            intakeSubsystem.rightPneumatic(false);
        } else if (!right && !motor) {
            intakeSubsystem.leftPneumatic(false);
        }

        if (motor) {
            intakeSubsystem.motorOff(right);
        }
    }
}
