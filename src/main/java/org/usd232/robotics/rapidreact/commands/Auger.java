package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;
import org.usd232.robotics.rapidreact.subsystems.EjectorSubsystem;

import static edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Auger extends CommandBase {

    private final AugerSubsystem augerSubsystem;
    private final EjectorSubsystem ejectorSubsystem;
    private final XboxController xbox;
    private final Alliance currentAlliance;
    private final String ballColor;
    
    /** Command that controls the color sensor, the auger,  */
    public Auger(AugerSubsystem augerSubsystem, EjectorSubsystem ejectorSubsystem, XboxController xbox) {
        this.augerSubsystem = augerSubsystem;
        this.ejectorSubsystem = ejectorSubsystem;
        this.xbox = xbox;
        this.currentAlliance = DriverStation.getAlliance();
        this.ballColor = EjectorSubsystem.getMatchedBallColor();
    }

    @Override
    public void execute() {
        augerSubsystem.elevatorOn(!xbox.getBackButton());

        if (currentAlliance != Alliance.Invalid) {  // Checks if the current alliance is valid (aka should use the color sensor)
            if (ballColor == "Red" && currentAlliance == Alliance.Blue) {
                // TODO: ejectorSubsystem.eject();
            } else if (ballColor == "Blue" && currentAlliance == Alliance.Red) {
                // TODO: ejectorSubsystem.eject();
            }
        }

        if (EjectorSubsystem.ejectorLS.get()) {
            new WaitCommand(1);
            ejectorSubsystem.resetEjecter();
        }

    }

    @Override
    public void end(boolean interrupted) {
        augerSubsystem.elevatorOff();
        ejectorSubsystem.resetEjecter();
    }
}
