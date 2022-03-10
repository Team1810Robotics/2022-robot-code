package org.usd232.robotics.rapidreact.commands.autonomous.paths;

import org.usd232.robotics.rapidreact.commands.autonomous.AugerOn;
import org.usd232.robotics.rapidreact.commands.autonomous.ShooterOn;
import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class OffLine extends SequentialCommandGroup {
    public OffLine(DriveSubsystem driveSubsystem, ShooterSubsystem shooterSubsystem, AugerSubsystem augerSubsystem) {
        Trajectory trajectory1 = driveSubsystem.loadTrajectoryFromFile("offLine");

        addCommands(
            new InstantCommand(() -> {
                driveSubsystem.resetOdometry(trajectory1.getInitialPose());
                driveSubsystem.zeroGyroscope();
            }),
            new ShooterOn(shooterSubsystem, 0.435, true),
            new WaitCommand(2), // TODO: Test
            new AugerOn(augerSubsystem, true),
            new WaitCommand(5), // TODO: Test
            new AugerOn(augerSubsystem, false),
            new ShooterOn(shooterSubsystem, 0.0, false),
            driveSubsystem.createCommandForTrajectory(trajectory1, false).withTimeout(15).withName("offLine")
        );
    }
}