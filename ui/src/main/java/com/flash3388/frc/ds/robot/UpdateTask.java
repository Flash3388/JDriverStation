package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DriverStation;
import javafx.application.Platform;

public class UpdateTask implements Runnable {

    private final DriverStation mDriverStation;
    private final RobotControlImpl mRobotControl;
    private final RobotPowerStatusImpl mRobotPowerStatus;
    private final RobotUsageStatusImpl mRobotUsageStatus;
    private final ConnectionStatusImpl mConnectionStatus;

    public UpdateTask(DriverStation driverStation, RobotControlImpl robotControl,
                      RobotPowerStatusImpl robotPowerStatus, RobotUsageStatusImpl robotUsageStatus,
                      ConnectionStatusImpl connectionStatus) {
        mDriverStation = driverStation;
        mRobotControl = robotControl;
        mRobotPowerStatus = robotPowerStatus;
        mRobotUsageStatus = robotUsageStatus;
        mConnectionStatus = connectionStatus;
    }

    @Override
    public void run() {
        Platform.runLater(()-> {
            mRobotControl.enabledProperty().set(mDriverStation.isRobotEnabled());
            mRobotControl.controlModeProperty().setValue(RobotControlMode.fromDsControlMode(mDriverStation.getControlMode()));

            mRobotPowerStatus.voltageProperty().set(mDriverStation.getRobotVoltage());
            mRobotPowerStatus.maxVoltageProperty().set(mDriverStation.getMaximumRobotVoltage());

            mRobotUsageStatus.cpuUsageProperty().set(mDriverStation.getCpuUsage());
            mRobotUsageStatus.ramUsageProperty().set(mDriverStation.getRamUsage());
            mRobotUsageStatus.diskUsageProperty().set(mDriverStation.getDiskUsage());
            mRobotUsageStatus.canUtilizationProperty().set(mDriverStation.getCanUsage());

            mConnectionStatus.fmsConnectedProperty().set(mDriverStation.isConnectedToFms());
            mConnectionStatus.robotConnectedProperty().set(mDriverStation.isConnectedToRobot());
            mConnectionStatus.radioConnectedProperty().set(mDriverStation.isConnectedToRadio());
        });
    }
}
