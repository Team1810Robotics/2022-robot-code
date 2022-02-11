package org.usd232.robotics.rapidreact.subsystems;

import org.usd232.robotics.rapidreact.log.Logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** A command that can be called for testing */
public class FakeCommand extends CommandBase {
    /**
     * The logger.
     * 
     * @since 2018
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();
    
    /** the amount of times WaitCommand has been called */
    private static int callNumber = 0;

    /** Time In secs that you want to sleep the bot for */
    private static int sleepTimeSecs = 0;

    FakeCommand(int SleepTimeSecs) {
        callNumber++;
        LOG.debug("WaitCommand Called [Call Number: %d]", callNumber);
        SmartDashboard.putString("Fake command", "Call Number: " + callNumber);

        sleepTimeSecs = SleepTimeSecs;
        if (sleepTimeSecs <= 1000) {
            sleepTimeSecs *= 1000;
        }
    }

    /** Call WaitCommand(int) first */
    FakeCommand() {
        this(sleepTimeSecs);
    }

    @Override
    public void execute() {
        try {
            LOG.info("Sleeping for " + sleepTimeSecs + " secs");
            Thread.sleep(sleepTimeSecs);
        } catch (Exception e) {}
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        LOG.debug("Exiting WaitCommand [CallNumber: %d]", callNumber);
    }

}
