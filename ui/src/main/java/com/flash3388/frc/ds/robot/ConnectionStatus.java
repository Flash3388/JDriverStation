package com.flash3388.frc.ds.robot;

import javafx.beans.value.ObservableBooleanValue;

public interface ConnectionStatus {

    ObservableBooleanValue fmsConnectedProperty();
    ObservableBooleanValue robotConnectedProperty();
    ObservableBooleanValue radioConnectedProperty();
}
