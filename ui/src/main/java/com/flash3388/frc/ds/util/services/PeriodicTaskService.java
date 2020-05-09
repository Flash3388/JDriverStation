package com.flash3388.frc.ds.util.services;

import com.castle.concurrent.service.Service;
import com.castle.time.Time;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class PeriodicTaskService implements Service {

    private final ScheduledExecutorService mExecutorService;
    private final Supplier<Time> mRunInterval;
    private final Runnable mTask;

    private final AtomicReference<Future<?>> mRunFuture;

    public PeriodicTaskService(ScheduledExecutorService executorService, Supplier<Time> runInterval, Runnable task) {
        mExecutorService = executorService;
        mRunInterval = runInterval;
        mTask = task;

        mRunFuture = new AtomicReference<>();
    }

    @Override
    public synchronized void start() {
        if (isRunning()) {
            return;
        }

        Time interval = mRunInterval.get();
        Future<?> future = mExecutorService.scheduleAtFixedRate(mTask,
                interval.value(), interval.value(), interval.unit());

        mRunFuture.set(future);
    }

    @Override
    public synchronized void stop() {
        if (!isRunning()) {
            return;
        }

        Future<?> future = mRunFuture.getAndSet(null);
        if (future != null) {
            future.cancel(true);
        }
    }

    @Override
    public boolean isRunning() {
        return mRunFuture.get() != null;
    }
}
