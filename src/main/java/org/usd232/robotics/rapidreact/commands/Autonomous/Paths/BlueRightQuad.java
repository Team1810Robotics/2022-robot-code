package org.usd232.robotics.rapidreact.commands.Autonomous.Paths;

import org.usd232.robotics.rapidreact.commands.FakeCommand;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class BlueRightQuad extends SequentialCommandGroup {
    public BlueRightQuad(DriveSubsystem driveSubsystem) {
        Trajectory trajectory1 = driveSubsystem.loadTrajectoryFromFile("blueRightQuadPart1");
        Trajectory trajectory2 = driveSubsystem.loadTrajectoryFromFile("blueRightQuadPart2");

        addCommands(
            new InstantCommand(() -> {
                driveSubsystem.resetOdometry(trajectory1.getInitialPose());
            }),
            driveSubsystem.createCommandForTrajectory(trajectory1, false).withTimeout(15).withName("BlueRightQuad_1"),
            new FakeCommand(2),
            driveSubsystem.createCommandForTrajectory(trajectory2, false).withTimeout(15).withName("BlueRightQuad_2")
        );
    }
}
