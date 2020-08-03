package com.flash3388.frc.ds;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProgramPaths {

    private final Path mConfigDataPath;
    private final Path mLogFilesDir;

    ProgramPaths(String programDir) {
        mConfigDataPath = Paths.get(programDir,"config.json");
        mLogFilesDir = Paths.get(programDir, "logs");
    }

    public Path getConfigDataPath() {
        return mConfigDataPath;
    }

    public Path getLogFilesDir() {
        return mLogFilesDir;
    }

    public void createDirectories() throws IOException {
        Files.createDirectories(mLogFilesDir);
    }
}
