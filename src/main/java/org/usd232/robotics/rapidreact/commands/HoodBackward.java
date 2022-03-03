package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.log.Logger;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

// TODO: Delete
public class HoodBackward extends CommandBase {
    /**
     * The logger.
     * 
     * @since 2018/
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();
    
    public HoodBackward() {}

    @Override
    public void execute() {
        HoodSubsystem.reverseHood();
        LOG.info("Hood of reverse");
    }

    @Override
    public boolean isFinished() {
        if (HoodSubsystem.hoodLS.get()) {
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
