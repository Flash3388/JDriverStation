package com.flash3388.frc.ds.robot;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class RobotPowerStatusImpl implements RobotPowerStatus {

    private final DoubleProperty mVoltage;
    private final DoubleProperty mMaxVoltage;

    public RobotPowerStatusImpl() {
        mVoltage = new SimpleDoubleProperty();
        mMaxVoltage = new SimpleDoubleProperty();
    }

    @Override
    public DoubleProperty voltageProperty() {
        return mVoltage;
    }

    @Override
    public DoubleProperty maxVoltageProperty() {
        return mMaxVoltage;
    }
}
