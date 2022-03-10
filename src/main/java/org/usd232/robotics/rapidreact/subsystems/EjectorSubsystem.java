package org.usd232.robotics.rapidreact.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import static org.usd232.robotics.rapidreact.Constants.EjectorConstants;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;

import org.usd232.robotics.rapidreact.PicoColorSensor;
import org.usd232.robotics.rapidreact.Constants.PneumaticConstants;
import org.usd232.robotics.rapidreact.PicoColorSensor.RawColor;
import org.usd232.robotics.rapidreact.log.Logger;

import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class EjectorSubsystem extends SubsystemBase {
    /**
     * The logger.
     * 
     * @since 2018
     */
    //@SuppressWarnings("unused")
    private static final Logger LOG = new Logger();
    
    private static final Solenoid ballEjector = new Solenoid(PneumaticConstants.PH_CAN_ID, PneumaticsModuleType.REVPH, EjectorConstants.EJECTOR_PNEUMATIC);
    public static final Solenoid lockSolenoid = new Solenoid(PneumaticConstants.PH_CAN_ID, PneumaticsModuleType.REVPH, EjectorConstants.LOCK_PNEUMATIC);

    public static final DigitalInput ejectorLS = new DigitalInput(EjectorConstants.EJECTOR_LS);

    private static final PicoColorSensor m_colorSensor = new PicoColorSensor();

    private final static ColorMatch colorMatcher = new ColorMatch();
    private static ColorMatchResult match;

    /** Color of the Blue Ball */
    private final static Color BlueBall = new Color(0.0, 0.0, 0.7019607844);               // TODO: Test Color
    /** Color of the Red Ball */
    private final static Color RedBall = new Color(0.9294117648, 0.0, 0.1411764706);       // TODO: Test Color

    /** Opens the ejector hatch */
    public void eject() {           // TODO: Test
        lockSolenoid.set(true);
        new WaitCommand(0.5);
        ballEjector.set(true); 
    }

    /** closes the ejector hatch */
    public void resetEjecter() {    // TODO: Test
        ballEjector.set(false); 
        new WaitCommand(1);
        lockSolenoid.set(false);
    }

    /** @return the current color seen by the Photo Electric color sensor (color sensor) */
    public static RawColor getColor() {
        if (m_colorSensor.isSensor0Connected()) {
            return m_colorSensor.getRawColor0();

        } else if (m_colorSensor.isSensor1Connected()) {
            return m_colorSensor.getRawColor1();

        } else if (m_colorSensor.isSensor2Connected()) {
            return m_colorSensor.getRawColor2();

        }

        LOG.warn("No Color Sensor Connected");
        return m_colorSensor.getRawColor0();
    }

    /** 
     * Matches the color
     * @return a string that is {@code "Blue"}, {@code "Red"}, or {@code "Unknown"} depending on what is seen by the color sensor
     */
    public static String getMatchedBallColor() {
        colorMatcher.setConfidenceThreshold(0.95);

        colorMatcher.addColorMatch(BlueBall);
        colorMatcher.addColorMatch(RedBall);

        match = colorMatcher.matchClosestColor(new Color(getColor().red, getColor().green, getColor().blue));

        if (match.color == BlueBall && match.confidence >= 60.0) {
            return "Blue";
        } else if (match.color == RedBall  && match.confidence >= 60.0) {
            return "Red";
        } else {
            return "Unknown";
        }
    }

    /** Color Debug Stuff */
    public static void colorDebug() {
        SmartDashboard.putNumber("Red", getColor().red);
        SmartDashboard.putNumber("Green", getColor().green);
        SmartDashboard.putNumber("Blue", getColor().blue);
        SmartDashboard.putNumber("Confidence", match.confidence);
        SmartDashboard.putString("Detected Color", getMatchedBallColor());
    }
}