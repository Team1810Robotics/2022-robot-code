package org.usd232.robotics.rapidreact.commands;

import static org.usd232.robotics.rapidreact.Constants.HoodConstants;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodTarget extends CommandBase {

    private final double targetValue;
    private boolean forward;


    public HoodTarget(double targitValue) {
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
            HoodSubsystem.forwardHood();
        } else {
            HoodSubsystem.reverseHood();
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
        HoodSubsystem.stopHood();
    }
}