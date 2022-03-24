package com.flash3388.frc.ds;

import com.castle.code.NativeLibrary;
import com.castle.code.finder.NativeLibraryFinder;
import com.castle.code.loader.NativeLibraryLoader;
import com.castle.code.loader.TempNativeLibraryLoader;
import com.castle.concurrent.service.Service;
import com.castle.nio.PathMatching;
import com.castle.nio.temp.TempPath;
import com.castle.nio.temp.TempPathGenerator;
import com.castle.nio.zip.OpenZip;
import com.castle.nio.zip.Zip;
import com.castle.time.Clock;
import com.castle.time.Clocks;
import com.castle.time.Time;
import com.castle.util.closeables.Closer;
import com.castle.util.java.JavaSources;
import com.castle.util.os.OperatingSystem;
import com.castle.util.regex.Patterns;
import com.flash3388.flashlib.util.logging.LogLevel;
import com.flash3388.flashlib.util.logging.LoggerBuilder;
import com.flash3388.flashlib.util.logging.jul.JsonFormatter;
import com.flash3388.frc.ds.comp.ComputerStatus;
import com.flash3388.frc.ds.computer.ComputerMonitor;
import com.flash3388.frc.ds.computer.ComputerStatusContainer;
import com.flash3388.frc.ds.configuration.Configuration;
import com.flash3388.frc.ds.configuration.ConfigurationFactory;
import com.flash3388.frc.ds.robot.DriverStationControlImpl;
import com.flash3388.frc.ds.robot.UpdateTask;
import com.flash3388.frc.ds.ui.UserInterface;
import com.flash3388.frc.ds.ui.WindowConfig;
import com.flash3388.frc.ds.util.ErrorHandler;
import com.flash3388.frc.ds.util.ImageLoader;
import com.flash3388.frc.ds.util.LoggerErrorHandler;
import com.flash3388.frc.ds.util.services.PeriodicTaskService;
import com.notifier.Controllers;
import com.notifier.EventController;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static net.sourceforge.argparse4j.impl.Arguments.storeTrue;

public class Main {

    private static final String SDLJNI_LIBNAME = "libsdl2_jni";

    public static void main(String[] args) throws Exception {
        ProgramOptions programOptions = handleArguments(args);

        String path = System.getProperty("user.dir");
        ProgramPaths programPaths = new ProgramPaths(path);

        if (programOptions.canUseFiles()) {
            programPaths.createDirectories();
        }

        Logger logger = createLogger(programPaths, programOptions);
        ErrorHandler errorHandler = new LoggerErrorHandler(logger);

        loadNatives();

        Configuration configuration = ConfigurationFactory.create(
            programPaths.getConfigDataPath(), logger, errorHandler,
                programOptions.canUseFiles());

        WindowConfig windowConfig = new WindowConfig(800, 300, false);

        Clock clock = Clocks.systemNanosClock();

        Closer closer = Closer.empty();
        CountDownLatch runLatch = new CountDownLatch(1);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(8);
        closer.add(executorService::shutdownNow);
        EventController eventController = Controllers.newExecutorBasedController(executorService);

        Collection<Service> services = new ArrayList<>();

        ComputerStatusContainer computerStatusContainer = new ComputerStatusContainer(eventController);
        ComputerMonitor computerMonitor = new ComputerMonitor(computerStatusContainer);
        services.add(computerMonitor.createService(executorService, ()-> Time.milliseconds(50)));

        DriverStationControlImpl driverStationControl = new DriverStationControlImpl();
        services.add(new PeriodicTaskService(executorService, ()->Time.milliseconds(50),
                new UpdateTask(driverStationControl)));

        DependencyHolder dependencyHolder = new DependencyHolder(
                clock,
                driverStationControl,
                computerStatusContainer, computerStatusContainer, computerStatusContainer,
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

    private static ProgramOptions handleArguments(String[] args) throws ArgumentParserException {
        ArgumentParser parser = ArgumentParsers.newFor("JDriverStation").build()
                .defaultHelp(true)
                .description("Control software for FRC robots");
        parser.addArgument("-d", "--debug")
                .required(false)
                .action(storeTrue())
                .help("Run in debug mode");
        parser.addArgument("-n", "--nofiles")
                .required(false)
                .action(storeTrue())
                .help("Run without outputting files, volatile mode");

        int startArgsIndex = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains(Main.class.getName())) {
                startArgsIndex = i;
                break;
            }
        }
        args = Arrays.copyOfRange(args, startArgsIndex+1, args.length);

        System.out.println(Arrays.toString(args));
        Namespace namespace = parser.parseArgs(args);

        return new ProgramOptions(
                namespace.getBoolean("debug"),
                !namespace.getBoolean("nofiles")
        );
    }

    private static Logger createLogger(ProgramPaths programPaths, ProgramOptions programOptions) throws IOException {
        LoggerBuilder loggerBuilder = new LoggerBuilder("jdriverstation")
                .enableConsoleLogging(true)
                .setLogLevel(programOptions.isDebug() ? LogLevel.DEBUG : LogLevel.INFO);

        if (programOptions.canUseFiles()) {
            Date date = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("hh_mm_ss");
            String filePattern = String.format("%s/log_%s.%%g.log",
                    programPaths.getLogFilesDir().toAbsolutePath().toString(), dateFormat.format(date));

            loggerBuilder.enableFileLogging(true)
                    .setFilePattern(filePattern)
                    .setFileLogFormatter(new JsonFormatter())
                    .enableDelegatedFileLogging(true);
        }

        return loggerBuilder.build();
    }

    private static void loadNatives() throws Exception {
        loadSdlNatives();
        loadNativesFromJar(ComputerStatus.class, Collections.singleton("libcomp"));
        loadNativesFromJar(com.flash3388.frc.ds.api.DriverStationJNI.class, Collections.singleton("libjds"));
    }

    private static void loadNativesFromJar(Class<?> classInJar, Collection<String> libs) throws Exception {
        Zip zip = JavaSources.containingJar(classInJar);

        try (OpenZip openZip = zip.open()) {
            Path basePath = openZip.pathFinder().findAll(
                    Patterns.wrapWithWildcards("natives/jni"),
                    PathMatching.directoryMatcher())
                    .iterator().next();

            NativeLibraryLoader nativeLibraryLoader = new TempNativeLibraryLoader();
            NativeLibraryFinder nativeLibraryFinder = new OurNativeLibraryFinder(zip, basePath);

            for (String lib : libs) {
                NativeLibrary nativeLibrary = nativeLibraryFinder.find(lib);
                nativeLibraryLoader.load(nativeLibrary);
            }
        }
    }

    private static void loadSdlNatives() {
        if (com.castle.util.os.System.operatingSystem() == OperatingSystem.Windows) {
            // windows has some problem with loading dependencies
            java.lang.System.loadLibrary("KERNEL32");
            java.lang.System.loadLibrary("msvcrt");
            java.lang.System.loadLibrary("SDL2");
        }

        try {
            loadSdl();
        } catch (Exception e) {
            throw new Error("Error loading natives for jsdl2", e);
        }
    }

    private static void loadSdl() throws Exception {
        switch (com.castle.util.os.System.operatingSystem()) {
            case Windows:
                Path sdlExtractPath = Paths.get(java.lang.System.getProperty("user.dir"))
                        .resolve(SDLJNI_LIBNAME + ".dll");
                if (!Files.exists(sdlExtractPath)) {
                    // not extracted
                    extractSdl(sdlExtractPath);
                }

                java.lang.System.load(sdlExtractPath.toAbsolutePath().toString());
                break;
            case Linux:
                try (TempPath tempPath = new TempPathGenerator().generateFile()) {
                    extractSdl(tempPath.originalPath());
                    java.lang.System.load(tempPath.originalPath().toAbsolutePath().toString());
                }
                break;
            default:
                throw new Error("Current operating system isn't support by jsdl2");
        }
    }

    private static void extractSdl(Path destination) throws IOException {
        String libPath;
        switch (com.castle.util.os.System.operatingSystem()) {
            case Windows:
                libPath = "/" + SDLJNI_LIBNAME + ".dll";
                break;
            case Linux:
                libPath = "/" + SDLJNI_LIBNAME + ".so";
                break;
            default:
                throw new UnsupportedOperationException();
        }

        try (InputStream inputStream = Main.class.getResourceAsStream(libPath)) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
