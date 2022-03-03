package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.Constants.HoodConstants;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodForward extends CommandBase {

    public HoodForward() {}

    @Override
    public void execute() {
        HoodSubsystem.forwardHood();
    }

    @Override
    public boolean isFinished() {
        if (HoodSubsystem.hoodEncoder.getDistance() <= HoodConstants.FORWARD_HOOD_LIMIT) {
            return true;
        }

        return false;
    }

    @Override
    public void end(boolean inturrupted) {
        HoodSubsystem.stopHood();
    }
}
