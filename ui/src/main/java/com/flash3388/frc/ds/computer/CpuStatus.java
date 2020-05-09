package com.flash3388.frc.ds.computer;

import javafx.beans.value.ObservableDoubleValue;

public class CpuStatus {

    private final ObservableDoubleValue mUsage;

    public CpuStatus(ObservableDoubleValue usage) {
        mUsage = usage;
    }

    public ObservableDoubleValue usageProperty() {
        return mUsage;
    }
}
