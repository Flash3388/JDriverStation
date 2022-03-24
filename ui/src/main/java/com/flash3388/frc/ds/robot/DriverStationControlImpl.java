package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DsControlMode;
import com.flash3388.frc.ds.api.DsProtocol;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DriverStationControlImpl implements DriverStationControl {

    private final BooleanProperty mFms;
    private final BooleanProperty mRobot;
    private final BooleanProperty mRadio;
    private final BooleanProperty mHasCode;
    private final StringProperty mStatusString;

    private final BooleanProperty mEnabled;
    private final Property<DsControlMode> mRobotControlMode;

    private final DoubleProperty mVoltage;
    private final DoubleProperty mMaxVoltage;

    private final IntegerProperty mTeamNumber;
    private final Property<TeamStation> mTeamStation;
    private final Property<DsProtocol> mProtocol;

    private final DoubleProperty mCpuUsage;
    private final DoubleProperty mRamUsage;
    private final DoubleProperty mDiskUsage;
    private final DoubleProperty mCanUsage;

    private final IntegerProperty mJoystickCount;

    public DriverStationControlImpl() {
        mFms = new SimpleBooleanProperty();
        mRobot = new SimpleBooleanProperty();
        mRadio = new SimpleBooleanProperty();
        mHasCode = new SimpleBooleanProperty();
        mStatusString = new SimpleStringProperty();

        mEnabled = new SimpleBooleanProperty();
        mRobotControlMode = new SimpleObjectProperty<>();

        mVoltage = new SimpleDoubleProperty();
        mMaxVoltage = new SimpleDoubleProperty();

        mTeamNumber = new SimpleIntegerProperty();
        mTeamStation = new SimpleObjectProperty<>();
        mProtocol = new SimpleObjectProperty<>();

        mCpuUsage = new SimpleDoubleProperty();
        mRamUsage = new SimpleDoubleProperty();
        mDiskUsage = new SimpleDoubleProperty();
        mCanUsage = new SimpleDoubleProperty();

        mJoystickCount = new SimpleIntegerProperty();
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
    public StringProperty statusStringProperty() {
        return mStatusString;
    }

    @Override
    public BooleanProperty enabledProperty() {
        return mEnabled;
    }

    @Override
    public Property<DsControlMode> controlModeProperty() {
        return mRobotControlMode;
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

    @Override
    public IntegerProperty joystickCountProperty() {
        return mJoystickCount;
    }

    @Override
    public boolean canEnableRobot() {
        return robotConnectedProperty().get() && robotHasCodeProperty().get();
    }

    @Override
    public void rebootRobot() {

    }

    @Override
    public void restartCode() {

    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setControlMode(DsControlMode controlMode) {

    }

    @Override
    public void setTeamNumber(int teamNumber) {

    }

    @Override
    public void setTeamStation(TeamStation teamStation) {

    }

    @Override
    public void setProtocol(DsProtocol protocol) {

    }
}
