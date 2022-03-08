package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Target extends CommandBase {

    private final DriveSubsystem driveSubsystem;
    private final VisionSubsystem visionSubsystem;

    public Target(DriveSubsystem driveSubsystem, VisionSubsystem visionSubsystem) {
        this.driveSubsystem = driveSubsystem;
        this.visionSubsystem = visionSubsystem;

        addRequirements(driveSubsystem, visionSubsystem);
    }

    /** Rotates the robot to target the goal using the limelight.
     *  Currently only targets on the X axis, since we don't have a hood yet.
     */
    @Override
    public void execute() {
        visionSubsystem.limeLightOn();
        // Stops targeting if the limelight sees no target
        if (visionSubsystem.targetValid() <= 1.0) { return; }


        // Checks if the crosshair is more or less than 1 degree away from the target
        if (visionSubsystem.targetXOffset() < -1.0 || visionSubsystem.targetXOffset() > 1.0) {

            // Rotates the robot, with the speed proportional to how close it is to the target for more accuracy
            // TODO These are just placeholder values
            driveSubsystem.drive(new ChassisSpeeds(0, 0, Math.toRadians(0.5 * visionSubsystem.targetXOffset())));
            
        }
        
    }

    // If the crosshair is within 1 degree of the target, then the robot will stop moving to prevent jiggle.
    @Override
    public boolean isFinished() {
        if (visionSubsystem.targetXOffset() > -1 || visionSubsystem.targetXOffset() < 1) {
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        visionSubsystem.limeLightOff();
        driveSubsystem.drive(new ChassisSpeeds (0, 0, 0));
    }
    
}