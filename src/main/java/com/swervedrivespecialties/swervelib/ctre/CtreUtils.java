package com.swervedrivespecialties.swervelib.ctre;

import com.ctre.phoenix.ErrorCode;

import edu.wpi.first.wpilibj.RobotBase;

public final class CtreUtils {
    private CtreUtils() {
    }

    public static void checkCtreError(ErrorCode errorCode, String message) {
        if (RobotBase.isReal() && errorCode != ErrorCode.OK) {
            throw new RuntimeException(String.format("%s: %s", message, errorCode.toString()));
        }
    }
}
