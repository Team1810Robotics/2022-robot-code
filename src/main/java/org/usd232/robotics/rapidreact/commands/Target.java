package org.usd232.robotics.rapidreact.commands;

import static org.usd232.robotics.rapidreact.Constants.HoodConstants;

import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;
import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Target extends CommandBase {

    private double[] targetValues;
    private final HoodSubsystem hoodSubsystem;
    private final VisionSubsystem visionSubsystem;
    private boolean forward;


    public Target(HoodSubsystem hoodSubsystem, VisionSubsystem visionSubsystem) {
        this.hoodSubsystem = hoodSubsystem;
        this.visionSubsystem = visionSubsystem;
    }


    /** Says whether the hood should move forward or backward based on the current encoder */
    @Override
    public void initialize() {
        this.targetValues = visionSubsystem.getTargetingValues();

        /* if (HoodSubsystem.hoodEncoder.getDistance() >= 0) {
            targetValues[0] = 0;
        } */

        if (targetValues[0] > HoodSubsystem.hoodEncoder.getDistance()) {
            forward = true;
        } else {
            forward = false;
        }
    }

    @Override
    public void execute() {

        // Don't start the hood if it's already within the targetValue 
        if (HoodSubsystem.hoodEncoder.getDistance() >= (targetValues[0] + 1) 
            || HoodSubsystem.hoodEncoder.getDistance() <= (targetValues[0] - 1)) {
                return;
        }

        if (forward) {
            hoodSubsystem.forwardHood();
        } else {
            hoodSubsystem.reverseHood();
        }
    }

    @Override
    public boolean isFinished() {
        if (HoodSubsystem.hoodEncoder.getDistance() >= HoodConstants.FORWARD_HOOD_LIMIT) {
            return true;
        }

        if (HoodSubsystem.hoodLS.get()) {
            return true;
        }

        if (HoodSubsystem.hoodEncoder.getDistance() >= (targetValues[0] + 1)) {
            return true;
        }

        if (HoodSubsystem.hoodEncoder.getDistance() <= (targetValues[0] - 1)) {
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean inturrupted) {
        hoodSubsystem.stopHood();
    }
}