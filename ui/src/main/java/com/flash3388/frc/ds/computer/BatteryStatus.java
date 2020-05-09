package com.flash3388.frc.ds.computer;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;

public interface BatteryStatus {

    ObservableDoubleValue levelProperty();
    ObservableBooleanValue isChargingProperty();
}
