package com.flash3388.frc.ds;

import com.castle.time.Clock;
import com.castle.time.SystemMillisClock;
import com.castle.util.closeables.Closer;
import com.flash3388.frc.ds.ui.UserInterface;
import com.flash3388.frc.ds.ui.WindowConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {

    public static void main(String[] args) throws Exception {

        WindowConfig windowConfig = new WindowConfig(500, 300, false);

        Clock clock = new SystemMillisClock();

        Closer closer = Closer.empty();
        CountDownLatch runLatch = new CountDownLatch(1);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        closer.add(executorService::shutdownNow);

        DependencyHolder dependencyHolder = DependencyHolder.create(executorService, clock);

        UserInterface userInterface = new UserInterface(executorService, windowConfig, dependencyHolder, runLatch::countDown);
        DriverStation driverStation = new DriverStation(userInterface);
        try {
            driverStation.start();
            runLatch.await();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            driverStation.stop();

            try {
                closer.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
