package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;
import org.usd232.robotics.rapidreact.subsystems.EjectorSubsystem;
import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;

import static edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ShootBalls extends CommandBase {

    private final AugerSubsystem augerSubsystem;
    private final EjectorSubsystem ejectorSubsystem;
    private final ShooterSubsystem shooterSubsystem;
    private final XboxController xbox;
    private final Alliance currentAlliance;
    private final String ballColor;
    
    /** Command that controls the color sensor, the auger,  */
    public ShootBalls(AugerSubsystem augerSubsystem, EjectorSubsystem ejectorSubsystem,
            ShooterSubsystem shooterSubsystem, XboxController xbox) {
        this.augerSubsystem = augerSubsystem;
        this.ejectorSubsystem = ejectorSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.xbox = xbox;
        this.currentAlliance = DriverStation.getAlliance();
        this.ballColor = EjectorSubsystem.getMatchedBallColor();
    }

    @Override
    public void execute() {
        augerSubsystem.elevatorOn(!xbox.getBackButton());

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

        // shooterSubsystem.shooterOn(1.0);   // TODO: test
    }

    @Override
    public void end(boolean inturrupted) {
        augerSubsystem.elevatorOff();
        // shooterSubsystem.shooterOff();
        ejectorSubsystem.resetEjecter();
    }
}
