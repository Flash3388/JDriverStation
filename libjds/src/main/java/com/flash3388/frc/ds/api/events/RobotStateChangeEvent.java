package com.flash3388.frc.ds.api.events;

import com.flash3388.frc.ds.api.DsControlMode;

public class RobotStateChangeEvent extends DsEvent {

    private final DsControlMode mControlMode;
    private final boolean mIsConnected;
    private final boolean mHasCode;
    private final boolean mIsEnabled;
    private final double mVoltage;
    private final boolean mIsEStoppped;
    private final double mCpuUsage;
    private final double mDiskUsage;
    private final double mRamUsage;
    private final double mCanUtilization;

    public RobotStateChangeEvent(DsControlMode controlMode,
                                 boolean isConnected, boolean hasCode, boolean isEnabled,
                                 double voltage, boolean isEStoppped,
                                 double cpuUsage, double diskUsage, double ramUsage, double canUtilization) {
        super(DsEventType.ROBOT_STATE_CHANGED);
        mControlMode = controlMode;
        mIsConnected = isConnected;
        mHasCode = hasCode;
        mIsEnabled = isEnabled;
        mVoltage = voltage;
        mIsEStoppped = isEStoppped;
        mCpuUsage = cpuUsage;
        mDiskUsage = diskUsage;
        mRamUsage = ramUsage;
        mCanUtilization = canUtilization;
    }

    public DsControlMode getControlMode() {
        return mControlMode;
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    public boolean hasCode() {
        return mHasCode;
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }

    public double getVoltage() {
        return mVoltage;
    }

    public boolean isEStoppped() {
        return mIsEStoppped;
    }

    public double getCpuUsage() {
        return mCpuUsage;
    }

    public double getDiskUsage() {
        return mDiskUsage;
    }

    public double getRamUsage() {
        return mRamUsage;
    }

    public double getCanUtilization() {
        return mCanUtilization;
    }
}
