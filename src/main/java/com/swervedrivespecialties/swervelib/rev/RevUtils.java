package com.swervedrivespecialties.swervelib.rev;

import com.revrobotics.REVLibError;

import edu.wpi.first.wpilibj.RobotBase;

public final class RevUtils {
    private RevUtils() {}

    public static void checkNeoError(REVLibError error, String message) {
        if (RobotBase.isReal() && error != REVLibError.kOk) {
            throw new RuntimeException(String.format("%s: %s", message, error.toString()));
        }
    }
}
