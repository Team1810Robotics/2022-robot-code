package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.subsystems.DriveSubsystem;
import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Target extends CommandBase {

    private final DriveSubsystem driveSubsystem;
    private final VisionSubsystem visionSubsystem;

    /** Left an right targeting */
    public Target(DriveSubsystem driveSubsystem, VisionSubsystem visionSubsystem) {
        this.driveSubsystem = driveSubsystem;
        this.visionSubsystem = visionSubsystem;

        addRequirements(driveSubsystem, visionSubsystem);
    }

    /** 
     * Rotates the robot to target the goal using the limelight.
     * Only targets on the X axis.
     */
    @Override
    public void execute() {
        
        // Checks if the crosshair is more or less than 1 degree away from the target
        if (visionSubsystem.targetXOffset() < -1.0 || visionSubsystem.targetXOffset() > 1.0) {
            
            // Rotates the robot, with the speed proportional to how close it is to the target for more accuracy
            // driveSubsystem.drive(new ChassisSpeeds(0, 0, (100 * visionSubsystem.targetXOffset())));
        
            // TODO: Test
            new SwerveDrive(driveSubsystem, 
                        () -> 0.0,
                        () -> 0.0, 
                        () -> (100 * visionSubsystem.targetXOffset()),
                        false);
        }
        
    }
    
    @Override
    public boolean isFinished() {
        // If the crosshair is within 1 degree of the target, then the robot will stop moving to prevent jiggle.
        if (visionSubsystem.targetXOffset() < -1 || visionSubsystem.targetXOffset() < 1) {
            return true;
        }
        
        // Stops targeting if the limelight sees no target
        if (visionSubsystem.targetValid() <= 1.0) {
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.drive(new ChassisSpeeds (0, 0, 0));
    }
    
}