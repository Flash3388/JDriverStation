package com.flash3388.frc.ds.computer;

import com.castle.concurrent.service.Service;
import com.castle.time.Time;
import com.flash3388.frc.ds.comp.ComputerStatus;
import com.flash3388.frc.ds.util.services.PeriodicTaskService;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

public class ComputerMonitor {

    private final ComputerStatus mComputerStatus;
    private final ComputerStatusContainer mComputerStatusContainer;

    public ComputerMonitor(ComputerStatus computerStatus, ComputerStatusContainer computerStatusContainer) {
        mComputerStatus = computerStatus;
        mComputerStatusContainer = computerStatusContainer;
    }

    public ComputerMonitor() {
        this(new ComputerStatus(), new ComputerStatusContainer());
    }

    public Service createService(ScheduledExecutorService executorService, Supplier<Time> runInterval) {
        return new PeriodicTaskService(executorService, runInterval,
                new UpdateTask(mComputerStatus, mComputerStatusContainer));
    }
}
