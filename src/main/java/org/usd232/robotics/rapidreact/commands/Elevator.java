package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;
import org.usd232.robotics.rapidreact.subsystems.EjectorSubsystem;

import static edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Elevator extends CommandBase {

    private final AugerSubsystem augerSubsystem;
    private final EjectorSubsystem ejectorSubsystem;
    private final Alliance currentAlliance;
    private final String ballColor;
    
    public Elevator(AugerSubsystem augerSubsystem, EjectorSubsystem ejectorSubsystem) {
        this.augerSubsystem = augerSubsystem;
        this.ejectorSubsystem = ejectorSubsystem;
        this.currentAlliance = DriverStation.getAlliance();
        this.ballColor = EjectorSubsystem.getMatchedBallColor();
    }

    @Override
    public void execute() {
        augerSubsystem.elevatorOn();

        if (currentAlliance != Alliance.Invalid) {  // Checks if the current alliance is valid (aka should use the color sensor)
            if (ballColor == "Red" && currentAlliance == Alliance.Blue) {
                ejectorSubsystem.eject();
            } else if (ballColor == "Blue" && currentAlliance == Alliance.Red) {
                ejectorSubsystem.eject();
            }
        }

        if (EjectorSubsystem.ejectorLS.get()) {
            new WaitCommand(0.5);
            ejectorSubsystem.resetEjecter();
        }
    }

    @Override
    public void end(boolean inturrupted) {
        augerSubsystem.elevatorOff();
        ejectorSubsystem.resetEjecter();
    }
}
