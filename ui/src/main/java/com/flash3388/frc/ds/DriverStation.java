package com.flash3388.frc.ds;

import com.flash3388.frc.ds.ui.UserInterface;
import com.flash3388.frc.ds.ui.exceptions.LaunchException;

public class DriverStation {

    private final UserInterface mUserInterface;

    public DriverStation(UserInterface userInterface) {
        mUserInterface = userInterface;
    }

    public void start() throws LaunchException {
        mUserInterface.launch();
    }

    public void stop() {
        mUserInterface.shutdown();
    }
}
