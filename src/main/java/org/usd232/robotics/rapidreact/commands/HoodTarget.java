package org.usd232.robotics.rapidreact.commands;

import static org.usd232.robotics.rapidreact.Constants.HoodConstants;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodTarget extends CommandBase {
    
    private final HoodSubsystem hoodSubsystem;
    private final double targetValue;
    private boolean forward;


    public HoodTarget(HoodSubsystem hoodSubsystem, double targitValue) {
        this.hoodSubsystem = hoodSubsystem;
        this.targetValue = targitValue;
    }


    /** Says whether the hood should move forward or backward based on the current encoder */
    @Override
    public void initialize() {
        if (targetValue > HoodSubsystem.hoodEncoder.getDistance()) {
            forward = true;
        } else {
            forward = false;
        }
    }


    /** Moves the hood forward or backward based on the boolean in initialize() */
    @Override
    public void execute() {

        // Don't start the hood if it's already within the targetValue
        // TODO: This might not be necessary 
        if (HoodSubsystem.hoodEncoder.getDistance() >= (targetValue + 1) 
            || HoodSubsystem.hoodEncoder.getDistance() <= (targetValue - 1)) { return; }

        if (forward) {
            hoodSubsystem.forwardHood();
        } else {
            hoodSubsystem.reverseHood();
        }
    }

    @Override
    public boolean isFinished() {
        if (HoodSubsystem.hoodEncoder.getDistance() >= HoodConstants.FORWARD_HOOD_LIMIT 
            | HoodSubsystem.hoodLS.get()) {
            return true;
        }

        if (HoodSubsystem.hoodEncoder.getDistance() >= (targetValue + 1)) {
            return true;
        }

        if (HoodSubsystem.hoodEncoder.getDistance() <= (targetValue - 1)) {
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean inturrupted) {
        hoodSubsystem.stopHood();
    }
}
/*
    public void setHood(double targetValue) {

        // If the hood is lower than it should be, move it forward
        if (hoodEncoder.get() < targetValue) {
            forwardHood();
        } else if (hoodEncoder.get() > targetValue) {
            reverseHood();
        }

        // When the hood is at to the target value, stop moving. This is the jank part.
        if(hoodEncoder.get() == targetValue) {
           stopHood();
           return;
        }

    }
*/