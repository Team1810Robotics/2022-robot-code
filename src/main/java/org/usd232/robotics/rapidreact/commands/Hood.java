package org.usd232.robotics.rapidreact.commands;

import static org.usd232.robotics.rapidreact.Constants.HoodConstants.*;
import org.usd232.robotics.rapidreact.log.Logger;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Hood extends CommandBase {
    /**
     * The logger.
     * 
     * @since 2018/
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();

    private static /*final*/ boolean forward;
    
    public Hood(boolean Forward) {
        forward = Forward;
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
        if (HoodSubsystem.hoodEncoder.getDistance() <= FORWARD_HOOD_LIMIT && forward) {
            return true;
        }
        
        if (HoodSubsystem.hoodLS.get() && !forward) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public void end(boolean inturrupted) {
        HoodSubsystem.zeroEncoder();
        HoodSubsystem.stopHood();
        LOG.info("Hood of stop");
    }
}