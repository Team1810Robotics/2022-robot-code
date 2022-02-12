package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Target extends CommandBase {

    private final DriveSubsystem driveSubsystem;

    public Target(DriveSubsystem driveSubsystem) {
        this.driveSubsystem = driveSubsystem;
        
        addRequirements(driveSubsystem);
    }

    /** Rotates the robot to target the goal using the limelight.
     *  Currently only targets on the X axis, since we don't have a hood yet.
     */
    @Override
    public void execute() {
        // Stops targeting if the limelight is off
        if (VisionSubsystem.llOff) {
            return;
        }

        // Turns the limelight LEDs on
        VisionSubsystem.limeLightOn();

        // Checks if the crosshair is more or less than 1 degree away from the target
        if (VisionSubsystem.targetXOffset < -1 || VisionSubsystem.targetXOffset > 1) {

            // Rotates the robot, with the speed proportional to how close it is to the target for more accuracy
            driveSubsystem.drive(new ChassisSpeeds(0, 0, 0.5 * VisionSubsystem.targetXOffset));
        }

    }

    // If the crosshair is within 1 degree of the target, then the robot will stop moving to prevent jiggle.
    @Override
    public boolean isFinished() {
        if (VisionSubsystem.targetXOffset > -1 || VisionSubsystem.targetXOffset < 1) {
            return true;
        }
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.drive(new ChassisSpeeds (0, 0, 0));
    }
    
}
