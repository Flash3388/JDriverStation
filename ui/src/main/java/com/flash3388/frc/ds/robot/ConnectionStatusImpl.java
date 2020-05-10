package com.flash3388.frc.ds.robot;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ConnectionStatusImpl implements ConnectionStatus {

    private final BooleanProperty mFms;
    private final BooleanProperty mRobot;
    private final BooleanProperty mRadio;

    public ConnectionStatusImpl() {
        mFms = new SimpleBooleanProperty();
        mRobot = new SimpleBooleanProperty();
        mRadio = new SimpleBooleanProperty();
    }

    @Override
    public BooleanProperty fmsConnectedProperty() {
        return mFms;
    }

    @Override
    public BooleanProperty robotConnectedProperty() {
        return mRobot;
    }

    @Override
    public BooleanProperty radioConnectedProperty() {
        return mRadio;
    }
}
