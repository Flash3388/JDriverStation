package com.flash3388.frc.ds.robot;

import javafx.beans.property.ReadOnlyDoubleProperty;

public class RobotUsageStatus {

    private final ReadOnlyDoubleProperty mCpuUsage;
    private final ReadOnlyDoubleProperty mRamUsage;
    private final ReadOnlyDoubleProperty mDiskUsage;
    private final ReadOnlyDoubleProperty mCanUtilization;

    public RobotUsageStatus(ReadOnlyDoubleProperty cpuUsage, ReadOnlyDoubleProperty ramUsage, ReadOnlyDoubleProperty diskUsage, ReadOnlyDoubleProperty canUtilization) {
        mCpuUsage = cpuUsage;
        mRamUsage = ramUsage;
        mDiskUsage = diskUsage;
        mCanUtilization = canUtilization;
    }

    public ReadOnlyDoubleProperty cpuUsageProperty() {
        return mCpuUsage;
    }

    public ReadOnlyDoubleProperty ramUsageProperty() {
        return mRamUsage;
    }

    public ReadOnlyDoubleProperty diskUsageProperty() {
        return mDiskUsage;
    }

    public ReadOnlyDoubleProperty canUtilizationProperty() {
        return mCanUtilization;
    }
}
