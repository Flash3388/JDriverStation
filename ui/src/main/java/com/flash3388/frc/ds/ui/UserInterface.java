package com.flash3388.frc.ds.ui;

import com.flash3388.frc.ds.DependencyHolder;
import com.flash3388.frc.ds.ui.exceptions.LaunchException;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class UserInterface {

    private final ScheduledExecutorService mExecutorService;
    private final WindowConfig mWindowConfig;
    private final DependencyHolder mDependencyHolder;
    private final Runnable mShutdownTask;

    private final AtomicReference<Future<?>> mUpdateTaskFuture;

    public UserInterface(ScheduledExecutorService executorService, WindowConfig windowConfig, DependencyHolder dependencyHolder, Runnable shutdownTask) {
        mExecutorService = executorService;
        mWindowConfig = windowConfig;
        mDependencyHolder = dependencyHolder;
        mShutdownTask = shutdownTask;

        mUpdateTaskFuture = new AtomicReference<>();
    }

    public void launch() throws LaunchException {
        Pair<Stage, MainWindow> windowPair = DriverStationApplication.launch(mExecutorService, mWindowConfig, mDependencyHolder);
        Stage stage = windowPair.getKey();
        stage.setOnCloseRequest((e) -> mShutdownTask.run());

        Platform.runLater(()-> {
            windowPair.getValue().loadInitialValues(mDependencyHolder);
        });

        MainWindow mainWindow = windowPair.getValue();
        Future<?> future = mExecutorService.scheduleAtFixedRate(new UpdateTask(mainWindow, mDependencyHolder.getClock()),
                0, 50, TimeUnit.MILLISECONDS);
        mUpdateTaskFuture.set(future);
    }

    public void shutdown() {
        Future<?> future = mUpdateTaskFuture.getAndSet(null);
        if (future != null) {
            future.cancel(true);
        }

        Platform.exit();
    }
}
