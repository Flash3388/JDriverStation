package com.flash3388.frc.ds;

import com.flash3388.frc.ds.computer.BatteryStatus;
import com.flash3388.frc.ds.computer.CpuStatus;
import com.flash3388.frc.ds.control.RobotControl;
import com.flash3388.frc.ds.control.RobotControlMode;
import com.flash3388.frc.ds.util.ImageLoader;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class DependencyHolder {

    private final RobotControl mRobotControl;
    private final BatteryStatus mBatteryStatus;
    private final CpuStatus mCpuStatus;
    private final ImageLoader mImageLoader;

    public DependencyHolder(RobotControl robotControl, BatteryStatus batteryStatus, CpuStatus cpuStatus, ImageLoader imageLoader) {
        mRobotControl = robotControl;
        mBatteryStatus = batteryStatus;
        mCpuStatus = cpuStatus;
        mImageLoader = imageLoader;
    }

    public RobotControl getRobotControl() {
        return mRobotControl;
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

    public static DependencyHolder create() {
        return new DependencyHolder(
                new RobotControl() {
                    @Override
                    public void setEnabled(boolean enabled) {

                    }

                    @Override
                    public void setControlMode(RobotControlMode controlMode) {

                    }
                },
                new BatteryStatus(new SimpleDoubleProperty(0.5), new SimpleBooleanProperty(true)),
                new CpuStatus(new SimpleDoubleProperty(0.5)),
                new ImageLoader(DependencyHolder.class.getClassLoader())
        );
    }
}
