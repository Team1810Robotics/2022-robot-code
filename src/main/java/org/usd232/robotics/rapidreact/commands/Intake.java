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
    public void initialize() { /* I Was Here, so was I */ }

    @Override
    public void execute() {
        if (right) {
            intakeSubsystem.rightPneumatic(true);
            intakeSubsystem.rightMotor(!xbox.getBackButtonPressed());
            xbox.setRumble(RumbleType.kRightRumble, 1.0);
            xbox.setRumble(RumbleType.kLeftRumble, 1.0);
        } else {
            intakeSubsystem.leftPneumatic(true);
            intakeSubsystem.leftMotor(!xbox.getBackButtonPressed());
            xbox.setRumble(RumbleType.kRightRumble, 1.0);
            xbox.setRumble(RumbleType.kLeftRumble, 1.0);
        } 
    }

    @Override
    public void end(boolean inturrupted) {
        if (right) {
            intakeSubsystem.rightPneumatic(false);
            xbox.setRumble(RumbleType.kRightRumble, 0.0);
        } else {
            intakeSubsystem.leftPneumatic(false);
            xbox.setRumble(RumbleType.kLeftRumble, 0.0);
        }
        intakeSubsystem.motorOff(right); // turns off the motor that was on. Dont get confused please : )
    }
}
