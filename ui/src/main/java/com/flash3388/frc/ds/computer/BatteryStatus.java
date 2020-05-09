package com.flash3388.frc.ds.computer;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;

public class BatteryStatus {

    private final ObservableDoubleValue mLevel;
    private final ObservableBooleanValue mIsCharging;

    public BatteryStatus(ObservableDoubleValue level, ObservableBooleanValue isCharging) {
        mLevel = level;
        mIsCharging = isCharging;
    }

    public ObservableDoubleValue levelValue() {
        return mLevel;
    }

    public ObservableBooleanValue isChargingValue() {
        return mIsCharging;
    }
}
