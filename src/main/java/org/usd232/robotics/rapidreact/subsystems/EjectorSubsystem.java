package org.usd232.robotics.rapidreact.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static org.usd232.robotics.rapidreact.Constants.EjectorConstants;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class EjectorSubsystem extends SubsystemBase {
    
    private static final Solenoid ballEjector = new Solenoid(PneumaticsModuleType.REVPH, EjectorConstants.PNEUMATIC_PORT);
    
    private final static I2C.Port i2cPort = I2C.Port.kMXP;
    private final static ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
    private final static ColorMatch colorMatcher = new ColorMatch();

    private final static Color BlueBall = Color.kFirstBlue;    // TODO: Test Color
    private final static Color RedBall = Color.kFirstRed;      // TODO: Test Color


    public void eject() {
        ballEjector.set(true);
    }

    public void resetEjecter() {
        ballEjector.set(false);
    }

    public static Color getCurrentColor() {
        return colorSensor.getColor();
    }

    public static String getMatchedBallColor() {
        colorMatcher.addColorMatch(BlueBall);
        colorMatcher.addColorMatch(RedBall);

        ColorMatchResult match = colorMatcher.matchClosestColor(getCurrentColor());

        if (match.color == BlueBall) {
            return "Blue";
        } else if (match.color == RedBall) {
            return "Red";
        } else {
            return "Unknown";
        }
    }

    /** Color Debug Stuff */
    // TODO: For removal
    public static void colorDebug() {
        SmartDashboard.putNumber("Red", getCurrentColor().red);
        SmartDashboard.putNumber("Green", getCurrentColor().green);
        SmartDashboard.putNumber("Blue", getCurrentColor().blue);
        SmartDashboard.putString("Detected Color", getMatchedBallColor());
    }
}