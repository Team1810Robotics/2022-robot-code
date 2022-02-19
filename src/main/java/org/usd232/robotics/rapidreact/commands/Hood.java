package org.usd232.robotics.rapidreact.commands;

import org.usd232.robotics.rapidreact.Constants.HoodConstants;
import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Hood extends CommandBase {
    private boolean forward;
    private Debouncer m_debouncer;

    public Hood(boolean forward, double debounceTime) {
        this.forward = forward;
        // Makes debouncer "debounce" input for debounceTime on the rising edge of the input (from false to true)
        this.m_debouncer = new Debouncer(debounceTime, Debouncer.DebounceType.kRising);
    }

    public Hood(boolean forward) {
        this(forward, 0.1);
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
        if (m_debouncer.calculate(HoodSubsystem.hoodLS.get()) && !forward) {
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
