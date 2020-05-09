package com.flash3388.frc.ds.comp;

public class ComputerStatus {

    public BatteryState getBatteryState() {
        float[] outputData = new float[2];
        int result = ComputerStatusJNI.getBatteryState(outputData);
        if (result < 0) {
            throw new NativeException(result);
        }

        return new BatteryState(outputData[0],
                ChargeState.fromValue((int) outputData[1]));
    }

    public double getCpuUsage() {
        float[] outputData = new float[1];
        int result = ComputerStatusJNI.getCpuUsage(outputData);
        if (result < 0) {
            throw new NativeException(result);
        }

        return outputData[0];
    }
}
