package com.flash3388.frc.ds.computer;

import com.flash3388.frc.ds.comp.BatteryState;
import com.flash3388.frc.ds.comp.ComputerStatus;
import com.flash3388.frc.ds.comp.ChargeState;

public class UpdateTask implements Runnable {

    private final ComputerStatus mComputerStatus;
    private final ComputerStatusContainer mComputerStatusContainer;

    public UpdateTask(ComputerStatus computerStatus, ComputerStatusContainer computerStatusContainer) {
        mComputerStatus = computerStatus;
        mComputerStatusContainer = computerStatusContainer;
    }

    @Override
    public void run() {
        BatteryState batteryState = mComputerStatus.getBatteryState();
        mComputerStatusContainer.levelProperty().set(batteryState.getChargeLevel());
        mComputerStatusContainer.isChargingProperty().set(batteryState.getChargeState() != ChargeState.POWER_DISCHARGING);

        double cpuUsage = mComputerStatus.getCpuUsage();
        mComputerStatusContainer.usageProperty().set(cpuUsage);
    }
}
