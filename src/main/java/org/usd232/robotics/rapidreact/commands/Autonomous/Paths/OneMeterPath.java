package org.usd232.robotics.rapidreact.commands.autonomous.paths;

import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class OneMeterPath extends SequentialCommandGroup {
    public OneMeterPath(DriveSubsystem driveSubsystem) {
        Trajectory trajectory1 = driveSubsystem.loadTrajectoryFromFile("meterTest");

        addCommands(
            new InstantCommand(() -> {
                driveSubsystem.resetOdometry(trajectory1.getInitialPose());
            }),
            driveSubsystem.createCommandForTrajectory(trajectory1, false).withTimeout(15).withName("meterTest")
        );
    }
}
