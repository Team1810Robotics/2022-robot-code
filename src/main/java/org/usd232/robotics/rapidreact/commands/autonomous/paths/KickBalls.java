package org.usd232.robotics.rapidreact.commands.autonomous.paths;

import org.usd232.robotics.rapidreact.subsystems.AugerSubsystem;
import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.ShooterSubsystem;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class KickBalls extends SequentialCommandGroup {
    public KickBalls(DriveSubsystem driveSubsystem, ShooterSubsystem shooterSubsystem, AugerSubsystem augerSubsystem) {
        Trajectory trajectory1 = driveSubsystem.loadTrajectoryFromFile("pickUpBLop2_pt1");
        Trajectory trajectory2 = driveSubsystem.loadTrajectoryFromFile("pickUpBLop2_pt2");

        addCommands(
            new InstantCommand(() -> {
                driveSubsystem.resetOdometry(trajectory1.getInitialPose());
            }),
            new InstantCommand(() -> shooterSubsystem.setShooterVelocity(0.435)),  
            new WaitCommand(2),
            new InstantCommand(() -> augerSubsystem.elevatorOn(true)),
            new WaitCommand(5),
            new InstantCommand(() -> augerSubsystem.elevatorOff()),
            driveSubsystem.createCommandForTrajectory(trajectory1, false).withTimeout(15).withName("kickBalls_pt1"),
            new WaitCommand(0.1),
            driveSubsystem.createCommandForTrajectory(trajectory2, false).withTimeout(15).withName("kickBalls_pt2")
        );
    }
}
