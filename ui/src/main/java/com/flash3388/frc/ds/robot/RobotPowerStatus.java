package com.flash3388.frc.ds.robot;

import javafx.beans.property.ReadOnlyDoubleProperty;

public class RobotPowerStatus {

    private final ReadOnlyDoubleProperty mVoltage;
    private final ReadOnlyDoubleProperty mMaxVoltage;

    public RobotPowerStatus(ReadOnlyDoubleProperty voltage, ReadOnlyDoubleProperty maxVoltage) {
        mVoltage = voltage;
        mMaxVoltage = maxVoltage;
    }

    public ReadOnlyDoubleProperty voltageProperty() {
        return mVoltage;
    }

    public ReadOnlyDoubleProperty maxVoltageProperty() {
        return mMaxVoltage;
    }
}
