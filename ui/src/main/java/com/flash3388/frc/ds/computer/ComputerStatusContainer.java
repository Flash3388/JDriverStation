package com.flash3388.frc.ds.computer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ComputerStatusContainer implements BatteryStatus, CpuStatus {

    private final DoubleProperty mBatteryLevelProperty;
    private final BooleanProperty mBatteryChargingProperty;
    private final DoubleProperty mCpuUsageProperty;

    public ComputerStatusContainer(DoubleProperty batteryLevelProperty, BooleanProperty batteryChargingProperty, DoubleProperty cpuUsageProperty) {
        mBatteryLevelProperty = batteryLevelProperty;
        mBatteryChargingProperty = batteryChargingProperty;
        mCpuUsageProperty = cpuUsageProperty;
    }

    public ComputerStatusContainer() {
        this(new SimpleDoubleProperty(), new SimpleBooleanProperty(), new SimpleDoubleProperty());
    }

    @Override
    public DoubleProperty levelProperty() {
        return mBatteryLevelProperty;
    }

    @Override
    public BooleanProperty isChargingProperty() {
        return mBatteryChargingProperty;
    }

    @Override
    public DoubleProperty usageProperty() {
        return mCpuUsageProperty;
    }
}
