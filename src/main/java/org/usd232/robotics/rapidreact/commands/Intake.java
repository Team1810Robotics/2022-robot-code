package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Intake extends CommandBase {

    private final IntakeSubsystem intakeSubsystem;
    private final XboxController xbox;
    private boolean right;
    
    public Intake(IntakeSubsystem intakeSubsystem, XboxController xbox, boolean right) {
        this.intakeSubsystem = intakeSubsystem;
        this.xbox = xbox;
        this.right = right;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() { /* I Was Here */ }

    @Override
    public void execute() {
        if (right) {
            intakeSubsystem.collectRight();
            xbox.setRumble(RumbleType.kRightRumble, 0.5);
        } else {
            intakeSubsystem.collectLeft();
            xbox.setRumble(RumbleType.kLeftRumble, 0.5);
        }
    }

    @Override
    public void end(boolean inturrupted) {
        if (right) {
            intakeSubsystem.returnRight();
            xbox.setRumble(RumbleType.kRightRumble, 0.0);
        } else {
            intakeSubsystem.returnLeft();
            xbox.setRumble(RumbleType.kLeftRumble, 0.0);
        }
    }
}
