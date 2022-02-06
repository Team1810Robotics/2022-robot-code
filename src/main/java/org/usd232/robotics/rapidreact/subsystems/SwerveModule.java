package org.usd232.robotics.rapidreact.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static org.usd232.robotics.rapidreact.Constants.ModuleConstants;

public class SwerveModule {

    private final WPI_TalonFX m_driveMotor;
    private final WPI_TalonFX m_steerMotor;

    private final CANCoder m_steerCANCoder;

    private final PIDController m_turningPidController;
    
    private final double m_moduleOffset;
    private final boolean m_CANCoderReversed;

    private final String m_moduleName;

    public SwerveModule(int driveMotorId, int steerMotorId,
                        boolean driveMotorReversed, boolean turningMotorReversed,
                        double moduleOffset, int CANCoderID, 
                        boolean CANCoderReversed, String moduleName) {
        this.m_moduleOffset = moduleOffset;
        m_steerCANCoder = new CANCoder(CANCoderID);
        m_CANCoderReversed = CANCoderReversed;

        m_driveMotor = new WPI_TalonFX(driveMotorId);
        m_steerMotor = new WPI_TalonFX(steerMotorId);

        m_driveMotor.setInverted(driveMotorReversed);
        m_steerMotor.setInverted(turningMotorReversed);

        m_turningPidController = new PIDController(ModuleConstants.kp_TURNING, 0, 0.1);
        m_turningPidController.enableContinuousInput(-Math.PI, Math.PI);

        m_moduleName = moduleName;

        // Set up encoder access for integrated encoder
        TalonFXConfiguration talonConfig = new TalonFXConfiguration();
        talonConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
        m_driveMotor.configAllSettings(talonConfig);
    }

    public SwerveModule(int driveMotorId, int steerMotorId,
                        boolean driveMotorReversed, boolean turningMotorReversed,
                        double moduleOffset,
                        int CANCoderID, boolean CANCoderReversed) {
        this(driveMotorId, steerMotorId,
             driveMotorReversed, turningMotorReversed,
             moduleOffset, CANCoderID,
             CANCoderReversed, "");
    }

    /** @return the Drive encoder clicks */
    public double getDrivePosition() {
        return m_driveMotor.getSelectedSensorPosition();
    }
    
    /** @return the CANCoder position */
    public double getSteerPosition() {
        return m_steerCANCoder.getAbsolutePosition();
    }

    /** @return the drive encoder clicks per 100ms */
    public double getDriveVelocity() {
        return m_driveMotor.getSelectedSensorVelocity();
    }

    /** @return the CANCoder Velocity as a double */
    public double getSteerVelocity() {
        return m_steerCANCoder.getVelocity();
    }

    /** @return the CANCoder position in radians */
    public double getCANCoderRadians() {
        double angle = Math.toRadians(m_steerCANCoder.getPosition());
        angle -= m_moduleOffset;
        return angle * (m_CANCoderReversed ? -1.0 : 1.0);
    }

    /** @return the module name */
    public String getName() {
        return m_moduleName;
    }

    /** retuns current module state as a {@link SwerveModuleState} */
    public SwerveModuleState getState() {
        return new SwerveModuleState(getDriveVelocity(), new Rotation2d(getSteerPosition()));
    }

    /** Takes in a desired state and applies it to the current object */
    public void setDesiredState(SwerveModuleState state) {
        // Allows the stopping to be much smoother
        if (Math.abs(state.speedMetersPerSecond) < 0.01) {
            stop();
            return;
        }

        // Applies the desired state to the Modules
        state = SwerveModuleState.optimize(state, getState().angle);
        m_driveMotor.set(ControlMode.PercentOutput,
                         state.speedMetersPerSecond / ModuleConstants.MAX_VELOCITY_METERS_PER_SECOND);
        m_steerMotor.set(ControlMode.PercentOutput,
                         m_turningPidController.calculate(getSteerPosition(), state.angle.getRadians()));
        SmartDashboard.putString("Swerve[ " + m_moduleName != "" ? m_moduleName
                                : m_steerCANCoder.getDeviceID() + " ] state", state.toString());
    }

    /** Stops current Module */
    public void stop() {
        m_driveMotor.set(ControlMode.PercentOutput, 0);
        m_steerMotor.set(ControlMode.PercentOutput, 0);
    }
}
