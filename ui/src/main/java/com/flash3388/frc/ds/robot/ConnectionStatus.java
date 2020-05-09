package com.flash3388.frc.ds.robot;

import javafx.beans.value.ObservableBooleanValue;

public class ConnectionStatus {

    private final ObservableBooleanValue mFmsConnected;
    private final ObservableBooleanValue mRobotConnected;
    private final ObservableBooleanValue mRadioConnected;

    public ConnectionStatus(ObservableBooleanValue fmsConnected, ObservableBooleanValue robotConnected, ObservableBooleanValue radioConnected) {
        mFmsConnected = fmsConnected;
        mRobotConnected = robotConnected;
        mRadioConnected = radioConnected;
    }

    public ObservableBooleanValue fmsConnectedProperty() {
        return mFmsConnected;
    }

    public ObservableBooleanValue robotConnectedProperty() {
        return mRobotConnected;
    }

    public ObservableBooleanValue radioConnectedProperty() {
        return mRadioConnected;
    }
}
