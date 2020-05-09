package com.flash3388.frc.ds.comp;

public class ComputerStatusJNI {

    private ComputerStatusJNI() {}

    public static native int getBatteryState(float[] outputData);
    public static native int getCpuUsage(float[] outputData);
}
