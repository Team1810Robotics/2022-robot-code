package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.log.Logger;

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
    
    /** the amount of times FakeCommand has been called */
    private static int callNumber = 0;

    /** Time In secs that you want to sleep the bot for */
    private static int sleepTimeSecs = 0;

    public FakeCommand(int SleepTimeSecs) {
        callNumber++;
        LOG.debug("FakeCommand Called [Call Number: %d]", callNumber);
        //SmartDashboard.putString("Fake command", "Call Number: " + callNumber);

        sleepTimeSecs = SleepTimeSecs;
        if (sleepTimeSecs <= 1000) {
            sleepTimeSecs *= 1000;
        }
    }

    /** @deprecated {@link FakeCommand#FakeCommand(int)} instead */
    @Deprecated(forRemoval = true)
    public FakeCommand() {
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
        LOG.debug("Exiting FakeCommand [CallNumber: %d]", callNumber);
    }

}
