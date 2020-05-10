package com.flash3388.frc.ds;

import com.castle.time.Clock;
import com.flash3388.frc.ds.computer.BatteryStatus;
import com.flash3388.frc.ds.computer.CpuStatus;
import com.flash3388.frc.ds.robot.ConnectionStatus;
import com.flash3388.frc.ds.robot.RobotControl;
import com.flash3388.frc.ds.robot.RobotControlMode;
import com.flash3388.frc.ds.robot.RobotPowerStatus;
import com.flash3388.frc.ds.robot.RobotUsageStatus;
import com.flash3388.frc.ds.util.ImageLoader;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.ExecutorService;

public class DependencyHolder {

    private final Clock mClock;
    private final RobotControl mRobotControl;
    private final RobotPowerStatus mRobotPowerStatus;
    private final RobotUsageStatus mRobotUsageStatus;
    private final ConnectionStatus mConnectionStatus;
    private final BatteryStatus mBatteryStatus;
    private final CpuStatus mCpuStatus;
    private final ImageLoader mImageLoader;

    public DependencyHolder(Clock clock,
                            RobotControl robotControl, RobotPowerStatus robotPowerStatus, RobotUsageStatus robotUsageStatus, ConnectionStatus connectionStatus,
                            BatteryStatus batteryStatus, CpuStatus cpuStatus,
                            ImageLoader imageLoader) {
        mClock = clock;
        mRobotControl = robotControl;
        mRobotPowerStatus = robotPowerStatus;
        mRobotUsageStatus = robotUsageStatus;
        mConnectionStatus = connectionStatus;
        mBatteryStatus = batteryStatus;
        mCpuStatus = cpuStatus;
        mImageLoader = imageLoader;
    }

    public Clock getClock() {
        return mClock;
    }

    public RobotControl getRobotControl() {
        return mRobotControl;
    }

    public RobotPowerStatus getRobotPowerStatus() {
        return mRobotPowerStatus;
    }

    public RobotUsageStatus getRobotUsageStatus() {
        return mRobotUsageStatus;
    }

    public ConnectionStatus getConnectionStatus() {
        return mConnectionStatus;
    }

    public BatteryStatus getBatteryStatus() {
        return mBatteryStatus;
    }

    public CpuStatus getCpuStatus() {
        return mCpuStatus;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
