package org.usd232.robotics.rapidreact.commands.autonomous.paths;

import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class OfflineReversed extends SequentialCommandGroup {
    public OfflineReversed(DriveSubsystem driveSubsystem, ShooterSubsystem shooterSubsystem, AugerSubsystem augerSubsystem) {
        Trajectory trajectory1 = driveSubsystem.loadTrajectoryFromFile("offLineReversed");

        addCommands(
            new InstantCommand(() -> {
                driveSubsystem.resetOdometry(trajectory1.getInitialPose());
            }),
            new InstantCommand(() -> shooterSubsystem.shooterOn(0.435)),
            new WaitCommand(2),
            new InstantCommand(() -> augerSubsystem.elevatorOn(true)),
            new WaitCommand(5),
            new InstantCommand(() -> augerSubsystem.elevatorOff()),
            new InstantCommand(() -> DriveSubsystem.coast = false),
            driveSubsystem.createCommandForTrajectory(trajectory1, false).withTimeout(15).withName("offLineReversed"),
            new InstantCommand(() -> DriveSubsystem.coast = true)
        );
    }
}