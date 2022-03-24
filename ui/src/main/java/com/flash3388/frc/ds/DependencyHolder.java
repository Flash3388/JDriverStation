package com.flash3388.frc.ds;

import com.castle.time.Clock;
import com.flash3388.frc.ds.computer.BatteryStatus;
import com.flash3388.frc.ds.computer.CpuStatus;
import com.flash3388.frc.ds.computer.hid.HidStatus;
import com.flash3388.frc.ds.robot.DriverStationControl;
import com.flash3388.frc.ds.util.ImageLoader;

public class DependencyHolder {

    private final Clock mClock;
    private final DriverStationControl mDriverStationControl;
    private final BatteryStatus mBatteryStatus;
    private final CpuStatus mCpuStatus;
    private final HidStatus mHidStatus;
    private final ImageLoader mImageLoader;

    public DependencyHolder(Clock clock,
                            DriverStationControl driverStationControl,
                            BatteryStatus batteryStatus, CpuStatus cpuStatus,
                            HidStatus hidStatus, ImageLoader imageLoader) {
        mClock = clock;
        mDriverStationControl = driverStationControl;
        mBatteryStatus = batteryStatus;
        mCpuStatus = cpuStatus;
        mHidStatus = hidStatus;
        mImageLoader = imageLoader;
    }

    public Clock getClock() {
        return mClock;
    }

    public DriverStationControl getDriverStationControl() {
        return mDriverStationControl;
    }

    public BatteryStatus getBatteryStatus() {
        return mBatteryStatus;
    }

    public CpuStatus getCpuStatus() {
        return mCpuStatus;
    }

    public HidStatus getHidStatus() {
        return mHidStatus;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
