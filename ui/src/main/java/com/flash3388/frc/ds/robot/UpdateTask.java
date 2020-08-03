package com.flash3388.frc.ds.robot;

import javafx.application.Platform;

public class UpdateTask implements Runnable {

    private final DriverStationControlImpl mControl;

    public UpdateTask(DriverStationControlImpl control) {
        mControl = control;
    }

    @Override
    public void run() {
        Platform.runLater(mControl::update);
    }
}
