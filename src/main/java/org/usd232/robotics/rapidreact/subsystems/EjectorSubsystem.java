package org.usd232.robotics.rapidreact.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import static org.usd232.robotics.rapidreact.Constants.EjectorConstants;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

// https://clips.twitch.tv/TallGenerousWrenchPoooound

public class EjectorSubsystem extends SubsystemBase {
    
    private static final Solenoid ballEjector = new Solenoid(PneumaticsModuleType.REVPH, EjectorConstants.EJECTOR_PNEUMATIC_PORT);
    private static final Solenoid lockSolenoid = new Solenoid(PneumaticsModuleType.REVPH, EjectorConstants.LOCK_PNEUMATIC_PORT);
    private static final DigitalInput ejectorLS = new DigitalInput(EjectorConstants.LIMIT_SWITCH);
    
    private final static I2C.Port i2cPort = I2C.Port.kMXP;
    private final static ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
    private final static ColorMatch colorMatcher = new ColorMatch();

    /** Color of the Blue Ball */
    private final static Color BlueBall = new Color(0.0, 0.4, 0.7019607844);                        // TODO: Test Color

    /** Color of the Red Ball */
    private final static Color RedBall = new Color(0.9294117648, 0.1098039216, 0.1411764706);       // TODO: Test Color

    /** Opens the ejector hatch */
    public void eject() {
        lockSolenoid.set(false);
        ballEjector.set(true);
    }

    /** closes the ejector hatch */
    public void resetEjecter() {
        ballEjector.set(false);
        new WaitCommand(1);
        lockSolenoid.set(true);
    }

    /** @return the current color seen by the Photo Electric color sensor (color sensor) */
    public static Color getCurrentColor() {
        return colorSensor.getColor();
    }

    /** 
     * Matches the color
     * @return a string that is {@code "Blue"}, {@code "Red"}, or {@code "Unknown"} depending on what is seen by the color sensor
     */
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

    /** @return the LS on the ejector flap */
    public boolean getLS() {
        return ejectorLS.get();
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
