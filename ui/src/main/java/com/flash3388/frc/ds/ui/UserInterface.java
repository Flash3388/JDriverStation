package com.flash3388.frc.ds.ui;

import com.flash3388.frc.ds.DependencyHolder;
import com.flash3388.frc.ds.ui.exceptions.LaunchException;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.concurrent.ExecutorService;

public class UserInterface {

    private final ExecutorService mExecutorService;
    private final WindowConfig mWindowConfig;
    private final DependencyHolder mDependencyHolder;
    private final Runnable mShutdownTask;

    public UserInterface(ExecutorService executorService, WindowConfig windowConfig, DependencyHolder dependencyHolder, Runnable shutdownTask) {
        mExecutorService = executorService;
        mWindowConfig = windowConfig;
        mDependencyHolder = dependencyHolder;
        mShutdownTask = shutdownTask;
    }

    public synchronized void launch() throws LaunchException {
        Pair<Stage, MainWindow> windowPair = DriverStationApplication.launch(mExecutorService, mWindowConfig, mDependencyHolder);
        Stage stage = windowPair.getKey();
        stage.setOnCloseRequest((e) -> mShutdownTask.run());

        MainWindow mainWindow = windowPair.getValue();
    }

    public synchronized void shutdown() {
        Platform.exit();
    }
}
