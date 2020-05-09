package com.flash3388.frc.ds;

import com.flash3388.frc.ds.computer.BatteryStatus;
import com.flash3388.frc.ds.computer.CpuStatus;
import com.flash3388.frc.ds.robot.ConnectionStatus;
import com.flash3388.frc.ds.robot.RobotControl;
import com.flash3388.frc.ds.robot.RobotControlMode;
import com.flash3388.frc.ds.robot.RobotStatus;
import com.flash3388.frc.ds.util.ImageLoader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.concurrent.ExecutorService;

public class DependencyHolder {

    private final RobotControl mRobotControl;
    private final RobotStatus mRobotStatus;
    private final ConnectionStatus mConnectionStatus;
    private final BatteryStatus mBatteryStatus;
    private final CpuStatus mCpuStatus;
    private final ImageLoader mImageLoader;

    public DependencyHolder(RobotControl robotControl, RobotStatus robotStatus, ConnectionStatus connectionStatus,
                            BatteryStatus batteryStatus, CpuStatus cpuStatus,
                            ImageLoader imageLoader) {
        mRobotControl = robotControl;
        mRobotStatus = robotStatus;
        mConnectionStatus = connectionStatus;
        mBatteryStatus = batteryStatus;
        mCpuStatus = cpuStatus;
        mImageLoader = imageLoader;
    }

    public RobotControl getRobotControl() {
        return mRobotControl;
    }

    public RobotStatus getRobotStatus() {
        return mRobotStatus;
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

    public static DependencyHolder create(ExecutorService executorService) {
        BooleanProperty d = new SimpleBooleanProperty(false);
        executorService.execute(()-> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
                d.set(!d.get());
            }
        });

        return new DependencyHolder(
                new RobotControl() {
                    @Override
                    public void setEnabled(boolean enabled) { }
                    @Override
                    public void setControlMode(RobotControlMode controlMode) { }
                    @Override
                    public void rebootRoboRio() { }
                    @Override
                    public void restartCode() { }
                },
                new RobotStatus(new SimpleDoubleProperty(), new SimpleDoubleProperty(), new SimpleDoubleProperty(), new SimpleDoubleProperty()),
                new ConnectionStatus(new SimpleBooleanProperty(), new SimpleBooleanProperty(), new SimpleBooleanProperty()),
                new BatteryStatus(new SimpleDoubleProperty(0.5), d),
                new CpuStatus(new SimpleDoubleProperty(0.5)),
                new ImageLoader(DependencyHolder.class.getClassLoader())
        );
    }
}
