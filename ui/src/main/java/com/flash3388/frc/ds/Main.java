package com.flash3388.frc.ds;

import com.castle.code.NativeLibrary;
import com.castle.code.NativeLibraryFinder;
import com.castle.code.loader.NativeLibraryLoader;
import com.castle.code.loader.TempNativeLibraryLoader;
import com.castle.concurrent.service.Service;
import com.castle.nio.temp.TempPathGenerator;
import com.castle.nio.zip.ArchivedNativeLibraryFinder;
import com.castle.nio.zip.Zip;
import com.castle.time.Clock;
import com.castle.time.SystemMillisClock;
import com.castle.time.Time;
import com.castle.util.closeables.Closer;
import com.castle.util.java.JavaSources;
import com.flash3388.frc.ds.comp.ComputerStatus;
import com.flash3388.frc.ds.computer.ComputerMonitor;
import com.flash3388.frc.ds.computer.ComputerStatusContainer;
import com.flash3388.frc.ds.ui.UserInterface;
import com.flash3388.frc.ds.ui.WindowConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {

    public static void main(String[] args) throws Exception {
        loadNatives();

        WindowConfig windowConfig = new WindowConfig(500, 300, false);

        Clock clock = new SystemMillisClock();

        Closer closer = Closer.empty();
        CountDownLatch runLatch = new CountDownLatch(1);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        closer.add(executorService::shutdownNow);

        Collection<Service> services = new ArrayList<>();

        ComputerStatus computerStatus = new ComputerStatus();
        ComputerStatusContainer computerStatusContainer = new ComputerStatusContainer();
        ComputerMonitor computerMonitor = new ComputerMonitor(computerStatus, computerStatusContainer);
        services.add(computerMonitor.createService(executorService, ()-> Time.milliseconds(50)));

        DependencyHolder dependencyHolder = DependencyHolder.create(executorService, clock,
                computerStatusContainer, computerStatusContainer);

        UserInterface userInterface = new UserInterface(executorService, windowConfig, dependencyHolder, runLatch::countDown);
        DriverStation driverStation = new DriverStation(userInterface, services);
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

    private static void loadNatives() throws Exception {
        Zip zip = JavaSources.currentJar(ComputerStatus.class);

        NativeLibraryLoader nativeLibraryLoader = new TempNativeLibraryLoader(new TempPathGenerator());
        NativeLibraryFinder nativeLibraryFinder = new ArchivedNativeLibraryFinder(zip);

        NativeLibrary nativeLibrary = nativeLibraryFinder.find("libcomp");
        nativeLibraryLoader.load(nativeLibrary);
    }
}
