package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DriverStation;
import com.flash3388.frc.ds.api.DsProtocol;
import com.flash3388.frc.ds.configuration.Configuration;
import com.flash3388.frc.ds.configuration.ConfigurationKeys;
import com.flash3388.frc.ds.robot.beans.ProtocolProperty;
import com.flash3388.frc.ds.robot.beans.TeamNumberProperty;
import com.flash3388.frc.ds.robot.beans.TeamStationProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;

import java.io.IOException;

public class DriverStationControlImpl implements DriverStationControl {

    private final DriverStation mDriverStation;

    private final BooleanProperty mFms;
    private final BooleanProperty mRobot;
    private final BooleanProperty mRadio;
    private final BooleanProperty mHasCode;

    private final BooleanProperty mEnabled;
    private final Property<RobotControlMode> mRobotControlMode;

    private final DoubleProperty mVoltage;
    private final DoubleProperty mMaxVoltage;

    private final IntegerProperty mTeamNumber;
    private final Property<TeamStation> mTeamStation;
    private final Property<DsProtocol> mProtocol;

    private final DoubleProperty mCpuUsage;
    private final DoubleProperty mRamUsage;
    private final DoubleProperty mDiskUsage;
    private final DoubleProperty mCanUsage;

    public DriverStationControlImpl(DriverStation driverStation, Configuration configuration) throws IOException {
        mDriverStation = driverStation;

        mFms = new SimpleBooleanProperty();
        mRobot = new SimpleBooleanProperty();
        mRadio = new SimpleBooleanProperty();
        mHasCode = new SimpleBooleanProperty();

        mEnabled = new SimpleBooleanProperty();
        mRobotControlMode = new SimpleObjectProperty<>(RobotControlMode.TELEOPERATED);

        mVoltage = new SimpleDoubleProperty();
        mMaxVoltage = new SimpleDoubleProperty();

        mTeamNumber = new TeamNumberProperty(configuration.getInt(ConfigurationKeys.TEAM_NUMBER), driverStation);
        mTeamStation = new TeamStationProperty(driverStation, TeamStation.defaultValue());
        mProtocol = new ProtocolProperty(configuration.getInt(ConfigurationKeys.COMM_PROTOCOL), driverStation);

        mCpuUsage = new SimpleDoubleProperty();
        mRamUsage = new SimpleDoubleProperty();
        mDiskUsage = new SimpleDoubleProperty();
        mCanUsage = new SimpleDoubleProperty();
    }

    @Override
    public BooleanProperty fmsConnectedProperty() {
        return mFms;
    }
    @Override
    public BooleanProperty robotConnectedProperty() {
        return mRobot;
    }
    @Override
    public BooleanProperty radioConnectedProperty() {
        return mRadio;
    }
    @Override
    public BooleanProperty robotHasCodeProperty() {
        return mHasCode;
    }

    @Override
    public void setEnabled(boolean enabled) {
        mDriverStation.setRobotEnabled(enabled);
    }
    public boolean canEnableRobot() {
        return robotConnectedProperty().get() && robotHasCodeProperty().get();
    }
    @Override
    public void setControlMode(RobotControlMode controlMode) {
        mDriverStation.setControlMode(controlMode.toDsControlMode());
    }
    @Override
    public BooleanProperty enabledProperty() {
        return mEnabled;
    }
    @Override
    public Property<RobotControlMode> controlModeProperty() {
        return mRobotControlMode;
    }
    @Override
    public void rebootRobot() {
        mDriverStation.rebootRobot();
    }
    @Override
    public void restartCode() {
        mDriverStation.restartRobotCode();
    }

    @Override
    public DoubleProperty voltageProperty() {
        return mVoltage;
    }
    @Override
    public DoubleProperty maxVoltageProperty() {
        return mMaxVoltage;
    }

    @Override
    public IntegerProperty teamNumberProperty() {
        return mTeamNumber;
    }
    @Override
    public Property<TeamStation> teamStationProperty() {
        return mTeamStation;
    }
    @Override
    public Property<DsProtocol> protocolProperty() {
        return mProtocol;
    }

    @Override
    public DoubleProperty cpuUsageProperty() {
        return mCpuUsage;
    }
    @Override
    public DoubleProperty ramUsageProperty() {
        return mRamUsage;
    }
    @Override
    public DoubleProperty diskUsageProperty() {
        return mDiskUsage;
    }
    @Override
    public DoubleProperty canUtilizationProperty() {
        return mCanUsage;
    }

    void update() {
        fmsConnectedProperty().set(mDriverStation.isConnectedToFms());
        robotConnectedProperty().set(mDriverStation.isConnectedToRobot());
        radioConnectedProperty().set(mDriverStation.isConnectedToRadio());
        robotHasCodeProperty().set(mDriverStation.hasRobotCode());

        enabledProperty().set(mDriverStation.isRobotEnabled());
        controlModeProperty().setValue(RobotControlMode.fromDsControlMode(mDriverStation.getControlMode()));

        voltageProperty().set(mDriverStation.getRobotVoltage());
        maxVoltageProperty().set(mDriverStation.getMaximumRobotVoltage());

        cpuUsageProperty().set(mDriverStation.getCpuUsage());
        ramUsageProperty().set(mDriverStation.getRamUsage());
        diskUsageProperty().set(mDriverStation.getDiskUsage());
        canUtilizationProperty().set(mDriverStation.getCanUsage());

        //teamNumberProperty().set(mDriverStation.getTeamNumber());
        teamStationProperty().setValue(
                TeamStation.fromDriverStationData(mDriverStation.getTeamAlliance(), mDriverStation.getTeamPosition()));
    }
}
