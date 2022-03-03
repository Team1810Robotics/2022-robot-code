package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.IntakeSubsystem;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Intake extends CommandBase {

    
    private final IntakeSubsystem intakeSubsystem;
    private final XboxController xbox;
    private static boolean motorIntake;
    private static boolean right;
    
    public Intake(IntakeSubsystem intakeSubsystem, XboxController xbox, boolean Right, boolean MotorIntake) {

        this.intakeSubsystem = intakeSubsystem;
        this.xbox = xbox;
        right = Right;
        motorIntake = MotorIntake;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() { /* I Was Here */ }

    @Override
    public void execute() {
        if (right) {
            intakeSubsystem.rightPneumatic(true);
            intakeSubsystem.rightMotor(motorIntake);
            xbox.setRumble(RumbleType.kRightRumble, 1.0);
        } else {
            intakeSubsystem.leftPneumatic(true);
            intakeSubsystem.leftMotor(motorIntake);
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
