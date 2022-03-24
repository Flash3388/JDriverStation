package com.flash3388.frc.ds.computer;

import com.castle.concurrent.service.Service;
import com.castle.concurrent.service.TerminalServiceBase;
import com.castle.time.Time;
import com.flash3388.frc.ds.comp.ComputerStatus;
//import com.flash3388.frc.ds.util.services.PeriodicTaskService;
import com.castle.concurrent.service.PeriodicTaskService;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ComputerMonitor {

    private final ComputerStatusContainer mComputerStatusContainer;

    public ComputerMonitor(ComputerStatusContainer computerStatusContainer) {
        mComputerStatusContainer = computerStatusContainer;
    }

    public ComputerMonitor() {
        this(new ComputerStatusContainer());
    }

    public Service createService(ScheduledExecutorService executorService, Supplier<Time> runInterval) {
        /*return new PeriodicTaskService(executorService, runInterval,
                new UpdateTask(mComputerStatus, mComputerStatusContainer));*/
        /*return new PeriodicTaskService(executorService,
                new UpdateTask(mComputerStatus, mComputerStatusContainer),
                runInterval);*/
        return new Impl(executorService, new UpdateTask(mComputerStatusContainer, runInterval.get()));
    }

    private static class Impl extends TerminalServiceBase {

        private final ScheduledExecutorService mExecutorService;
        private final Runnable mTask;

        private Future<?> mTaskFuture;

        private Impl(ScheduledExecutorService executorService, Runnable task) {
            mExecutorService = executorService;
            mTask = task;
            mTaskFuture = null;
        }

        @Override
        protected void startRunning() {
            mTaskFuture = mExecutorService.submit(mTask);
        }

        @Override
        protected void stopRunning() {
            if (mTaskFuture != null) {
                mTaskFuture.cancel(true);
                mTaskFuture = null;
            }
        }
    }
}
