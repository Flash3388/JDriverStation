package com.flash3388.frc.ds.ui;

public class WindowConfig {

    private final double mWidth;
    private final double mHeight;
    private final boolean mIsFullScreen;

    public WindowConfig(double width, double height, boolean isFullScreen) {
        mWidth = width;
        mHeight = height;
        mIsFullScreen = isFullScreen;
    }

    public double getWidth() {
        return mWidth;
    }

    public double getHeight() {
        return mHeight;
    }

    public boolean isFullScreen() {
        return mIsFullScreen;
    }
}
