package org.usd232.robotics.rapidreact.commands.autonomous.paths;

import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class PickUpBalls extends SequentialCommandGroup {
    public PickUpBalls(DriveSubsystem driveSubsystem, ShooterSubsystem shooterSubsystem, AugerSubsystem augerSubsystem) {
        Trajectory trajectory1 = driveSubsystem.loadTrajectoryFromFile("pickUpBLop1_pt1");
        Trajectory trajectory2 = driveSubsystem.loadTrajectoryFromFile("pickUpBLop1_pt2");
        Trajectory trajectory3 = driveSubsystem.loadTrajectoryFromFile("pickUpBLop1_pt3");

        addCommands(
            new InstantCommand(() -> {
                driveSubsystem.resetOdometry(trajectory1.getInitialPose());
            }),
            new InstantCommand(() -> shooterSubsystem.setShooterVelocity(0.435)),  
            new WaitCommand(2),
            new InstantCommand(() -> augerSubsystem.elevatorOn(true)),
            new WaitCommand(5),
            new InstantCommand(() -> augerSubsystem.elevatorOff()),
            driveSubsystem.createCommandForTrajectory(trajectory1, false).withTimeout(15).withName("pickUpBalls_pt1"),
            driveSubsystem.createCommandForTrajectory(trajectory2, false).withTimeout(15).withName("pickUpBalls_pt2"),
            driveSubsystem.createCommandForTrajectory(trajectory3, false).withTimeout(15).withName("pickUpBalls_pt3")
        );
    }
}
