package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.Constants.HoodConstants;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Hood extends CommandBase {
    private boolean forward;

    public Hood(boolean forward) {
        this.forward = forward;
    }

    @Override
    public void execute() {
        if (forward) {
            HoodSubsystem.forwardHood();
        } else {
            HoodSubsystem.reverseHood();
        }
    }

    @Override
    public boolean isFinished() {
        if (HoodSubsystem.hoodLS.get() && !forward) {
            return true;
        }

        if (HoodSubsystem.hoodEncoder.getDistance() >= HoodConstants.FORWARD_HOOD_LIMIT && forward) {
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean inturrupted) {
        HoodSubsystem.stopHood();
    }
}
