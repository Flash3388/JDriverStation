package com.flash3388.frc.ds;

import com.castle.code.NativeLibrary;
import com.castle.code.NativeLibraryFinder;
import com.castle.code.loader.NativeLibraryLoader;
import com.castle.code.loader.TempNativeLibraryLoader;
import com.castle.concurrent.service.Service;
import com.castle.nio.PathMatching;
import com.castle.nio.temp.TempPathGenerator;
import com.castle.nio.zip.ArchivedNativeLibraryFinder;
import com.castle.nio.zip.OpenZip;
import com.castle.nio.zip.Zip;
import com.castle.time.Clock;
import com.castle.time.SystemMillisClock;
import com.castle.time.Time;
import com.castle.util.closeables.Closer;
import com.castle.util.java.JavaSources;
import com.castle.util.regex.Patterns;
import com.flash3388.frc.ds.comp.ComputerStatus;
import com.flash3388.frc.ds.computer.ComputerMonitor;
import com.flash3388.frc.ds.computer.ComputerStatusContainer;
import com.flash3388.frc.ds.robot.ConnectionStatus;
import com.flash3388.frc.ds.robot.ConnectionStatusImpl;
import com.flash3388.frc.ds.robot.RobotControl;
import com.flash3388.frc.ds.robot.RobotControlImpl;
import com.flash3388.frc.ds.robot.RobotControlMode;
import com.flash3388.frc.ds.robot.RobotPowerStatus;
import com.flash3388.frc.ds.robot.RobotPowerStatusImpl;
import com.flash3388.frc.ds.robot.RobotUsageStatus;
import com.flash3388.frc.ds.robot.RobotUsageStatusImpl;
import com.flash3388.frc.ds.robot.UpdateTask;
import com.flash3388.frc.ds.ui.UserInterface;
import com.flash3388.frc.ds.ui.WindowConfig;
import com.flash3388.frc.ds.util.ImageLoader;
import com.flash3388.frc.ds.util.services.PeriodicTaskService;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        loadNatives();

        WindowConfig windowConfig = new WindowConfig(800, 300, false);

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

        com.flash3388.frc.ds.api.DriverStation driverStation = com.flash3388.frc.ds.api.DriverStation.getInstance();
        RobotControlImpl robotControl = new RobotControlImpl(driverStation);
        RobotPowerStatusImpl robotPowerStatus = new RobotPowerStatusImpl();
        RobotUsageStatusImpl robotUsageStatus = new RobotUsageStatusImpl();
        ConnectionStatusImpl connectionStatus = new ConnectionStatusImpl();
        services.add(new PeriodicTaskService(executorService, ()->Time.milliseconds(50),
                new UpdateTask(driverStation, robotControl, robotPowerStatus, robotUsageStatus, connectionStatus)));

        DependencyHolder dependencyHolder = new DependencyHolder(
                clock,
                robotControl, robotPowerStatus, robotUsageStatus, connectionStatus,
                computerStatusContainer, computerStatusContainer,
                new ImageLoader(DependencyHolder.class.getClassLoader()));

        UserInterface userInterface = new UserInterface(executorService, windowConfig, dependencyHolder, runLatch::countDown);
        DriverStation ds = new DriverStation(userInterface, services);
        try {
            ds.start();
            runLatch.await();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            ds.stop();

            try {
                closer.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private static void loadNatives() throws Exception {
        loadNativesFromJar(ComputerStatus.class, "comp", Collections.singleton("libcomp"));
        loadNativesFromJar(com.flash3388.frc.ds.api.DriverStation.class, "jds", Collections.singleton("libjds"));
    }

    private static void loadNativesFromJar(Class<?> classInJar, String prefixPackage, Collection<String> libs) throws Exception {
        Zip zip = JavaSources.currentJar(classInJar);
        Path basePath;
        try (OpenZip openZip = zip.open()) {
            basePath = openZip.pathFinder().findAll(Patterns.wrapWithWildcards(prefixPackage),
                    PathMatching.directoryMatcher()).iterator().next();
        }

        NativeLibraryLoader nativeLibraryLoader = new TempNativeLibraryLoader(new TempPathGenerator());
        NativeLibraryFinder nativeLibraryFinder = new ArchivedNativeLibraryFinder(zip, basePath);

        for (String lib : libs) {
            NativeLibrary nativeLibrary = nativeLibraryFinder.find(lib);
            nativeLibraryLoader.load(nativeLibrary);
        }
    }
}
