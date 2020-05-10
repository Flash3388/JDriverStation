package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DriverStation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RobotControlImpl implements RobotControl {

    private final DriverStation mDriverStation;

    private final BooleanProperty mEnabled;
    private final Property<RobotControlMode> mRobotControlMode;

    public RobotControlImpl(DriverStation driverStation) {
        mDriverStation = driverStation;
        mEnabled = new SimpleBooleanProperty();
        mRobotControlMode = new SimpleObjectProperty<>(RobotControlMode.TELEOPERATED);
    }

    @Override
    public void setEnabled(boolean enabled) {
        mDriverStation.setRobotEnabled(enabled);
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
}
