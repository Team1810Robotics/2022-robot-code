package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Intake extends CommandBase {

    
    private final IntakeSubsystem intakeSubsystem;
    private final XboxController xbox;
    private final boolean right;
    private final boolean motors;
    
    public Intake(IntakeSubsystem intakeSubsystem, XboxController xbox, boolean right, boolean motors) {

        this.intakeSubsystem = intakeSubsystem;
        this.xbox = xbox;
        this.right = right;
        this.motors = motors;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() { /* I Was Here, so was I */ }

    @Override
    public void execute() {
        if (!motors) {
            if (right) {
                intakeSubsystem.rightPneumatic(true);
            } else {
                intakeSubsystem.leftPneumatic(true);
            }
        } else {
            if (right) {
                intakeSubsystem.rightMotor(!xbox.getBackButton());
            } else {
                intakeSubsystem.leftMotor(!xbox.getBackButton());
            }
            xbox.setRumble(RumbleType.kLeftRumble, 0.5);
            xbox.setRumble(RumbleType.kRightRumble, 0.5);
        }
    }

    @Override
    public void end(boolean inturrupted) {
        if (right) {
            intakeSubsystem.rightPneumatic(false);
        } else {
            intakeSubsystem.leftPneumatic(false);
        }
        intakeSubsystem.motorOff(right); // turns off the motor that was on. Dont get confused please : )
        xbox.setRumble(RumbleType.kRightRumble, 0.0);
        xbox.setRumble(RumbleType.kLeftRumble, 0.0);
    }
}
