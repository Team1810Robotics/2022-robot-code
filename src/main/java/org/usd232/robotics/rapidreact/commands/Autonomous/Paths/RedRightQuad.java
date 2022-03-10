package org.usd232.robotics.rapidreact.commands.autonomous.paths;

import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class RedRightQuad extends SequentialCommandGroup {
    public RedRightQuad(DriveSubsystem driveSubsystem) {
        Trajectory trajectory1 = driveSubsystem.loadTrajectoryFromFile("redRightQuadPart1");
        Trajectory trajectory2 = driveSubsystem.loadTrajectoryFromFile("redRightQuadPart2");

        addCommands(
            new InstantCommand(() -> {
                driveSubsystem.resetOdometry(trajectory1.getInitialPose());
            }),
            driveSubsystem.createCommandForTrajectory(trajectory1, false).withTimeout(15).withName("RedRightQuad_1"),
            new WaitCommand(2),
            driveSubsystem.createCommandForTrajectory(trajectory2, false).withTimeout(15).withName("RedRightQuad_2")
        );
    }
}
