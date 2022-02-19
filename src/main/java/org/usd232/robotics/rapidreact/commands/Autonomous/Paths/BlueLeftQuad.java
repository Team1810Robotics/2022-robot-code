package org.usd232.robotics.rapidreact.commands.Autonomous.Paths;

import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class BlueLeftQuad extends SequentialCommandGroup {
    public BlueLeftQuad(DriveSubsystem driveSubsystem) {
        Trajectory trajectory1 = driveSubsystem.loadTrajectoryFromFile("blueLeftQuad");

        addCommands(
            new InstantCommand(() -> {
                driveSubsystem.resetOdometry(trajectory1.getInitialPose());
            }),
            driveSubsystem.createCommandForTrajectory(trajectory1, false).withTimeout(15).withName("blueLeftQuad")
        );
    }
}
