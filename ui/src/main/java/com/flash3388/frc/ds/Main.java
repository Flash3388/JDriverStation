package com.flash3388.frc.ds;

import com.castle.util.closeables.Closer;
import com.flash3388.frc.ds.ui.UserInterface;
import com.flash3388.frc.ds.ui.WindowConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        WindowConfig windowConfig = new WindowConfig(500, 300, false);

        Closer closer = Closer.empty();
        CountDownLatch runLatch = new CountDownLatch(1);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        closer.add(executorService::shutdownNow);

        UserInterface userInterface = new UserInterface(executorService, windowConfig, runLatch::countDown);
        DriverStation driverStation = new DriverStation(userInterface);
        try {
            driverStation.start();
            runLatch.await();
        } catch (Throwable t) {

        } finally {
            driverStation.stop();
        }
    }
}
