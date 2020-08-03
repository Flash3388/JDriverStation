package com.flash3388.frc.ds;

public class ProgramOptions {

    private final boolean mDebug;
    private final boolean mUseFiles;

    ProgramOptions(boolean debug, boolean useFiles) {
        mDebug = debug;
        mUseFiles = useFiles;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public boolean canUseFiles() {
        return mUseFiles;
    }
}
