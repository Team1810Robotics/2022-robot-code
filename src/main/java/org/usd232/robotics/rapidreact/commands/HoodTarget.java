package org.usd232.robotics.rapidreact.commands;

import static org.usd232.robotics.rapidreact.Constants.HoodConstants;

import org.usd232.robotics.rapidreact.subsystems.HoodSubsystem;
import org.usd232.robotics.rapidreact.subsystems.VisionSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodTarget extends CommandBase { 

    private final HoodSubsystem hoodSubsystem;
    private final VisionSubsystem visionSubsystem;
    private double[] targetValues;
    private boolean stop;

    public HoodTarget(HoodSubsystem hoodSubsystem, VisionSubsystem visionSubsystem) {
        this.hoodSubsystem =  hoodSubsystem;
        this.visionSubsystem = visionSubsystem;
    }
    
    @Override
    public void initialize() {
        if ((visionSubsystem.getTargetingValues()[0] - 100) >= HoodSubsystem.hoodEncoder.getDistance() ||
        (visionSubsystem.getTargetingValues()[0] + 100) <= HoodSubsystem.hoodEncoder.getDistance()) {
            this.stop = true;
        }
    }
    
    @Override
    public void execute() {
        targetValues = visionSubsystem.getTargetingValues();
        
        if (targetValues[0] >= 0) {
            targetValues[0] = 0;
        }

        if (visionSubsystem.targetValid() >= 1 && !stop) {
            if (targetValues[0] > HoodSubsystem.hoodEncoder.getDistance()) {
                hoodSubsystem.forwardHood();
            } else if (targetValues[0] < HoodSubsystem.hoodEncoder.getDistance()) {
                hoodSubsystem.reverseHood();
            }  
        }
    }

    @Override
    public boolean isFinished() {
        if ((targetValues[0] - 100) >= HoodSubsystem.hoodEncoder.getDistance() ||
                (targetValues[0] + 100) <= HoodSubsystem.hoodEncoder.getDistance()) {
            return true;
        }

        if (targetValues[0] == HoodSubsystem.hoodEncoder.getDistance()) {
            return true;
        }

        if (HoodSubsystem.hoodEncoder.getDistance() >= HoodConstants.FORWARD_HOOD_LIMIT) {
            return true;
        }

        /* if (HoodSubsystem.hoodLS.get()) {
            return true;
        } */
        
        return false;
    }

    @Override
    public void end(boolean inturrupted) {
        hoodSubsystem.stopHood();
    }
}