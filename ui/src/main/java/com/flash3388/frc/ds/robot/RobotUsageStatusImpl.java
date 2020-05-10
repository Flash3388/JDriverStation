package com.flash3388.frc.ds.robot;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class RobotUsageStatusImpl implements RobotUsageStatus {

    private final DoubleProperty mCpuUsage;
    private final DoubleProperty mRamUsage;
    private final DoubleProperty mDiskUsage;
    private final DoubleProperty mCanUsage;

    public RobotUsageStatusImpl() {
        mCpuUsage = new SimpleDoubleProperty();
        mRamUsage = new SimpleDoubleProperty();
        mDiskUsage = new SimpleDoubleProperty();
        mCanUsage = new SimpleDoubleProperty();
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
}
