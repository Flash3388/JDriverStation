package com.flash3388.frc.ds.ui;

import com.flash3388.frc.ds.ui.exceptions.LaunchException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

public class DriverStationApplication extends Application {

    private static final CyclicBarrier sBarrier = new CyclicBarrier(2);

    private static volatile WindowConfig sWindowConfig;

    private static volatile DriverStationApplication sInstance;

    private final AtomicReference<Stage> mPrimaryStage = new AtomicReference<>();
    private final AtomicReference<MainWindow> mMainWindow = new AtomicReference<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            sInstance = this;
            mPrimaryStage.set(primaryStage);

            MainWindow mainWindow = new MainWindow(primaryStage);
            mMainWindow.set(mainWindow);

            sBarrier.await();

            primaryStage.setResizable(true);
            primaryStage.setTitle("JDriverStation");
            primaryStage.setFullScreen(sWindowConfig.isFullScreen());
            primaryStage.setScene(new Scene(mainWindow,
                    sWindowConfig.getWidth(), sWindowConfig.getHeight()));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Pair<Stage, MainWindow> launch(ExecutorService executorService, WindowConfig windowConfig) throws LaunchException {
        sWindowConfig = windowConfig;
        executorService.submit(()->Application.launch(DriverStationApplication.class));

        try {
            sBarrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new LaunchException(e);
        }

        return new Pair<>(sInstance.mPrimaryStage.get(),
                sInstance.mMainWindow.get());
    }
}
