package com.flash3388.frc.ds;

import com.castle.concurrent.service.Service;
import com.flash3388.frc.ds.ui.UserInterface;
import com.flash3388.frc.ds.ui.exceptions.LaunchException;

import java.util.Collection;

public class DriverStation {

    private final UserInterface mUserInterface;
    private final Collection<Service> mServices;

    public DriverStation(UserInterface userInterface, Collection<Service> services) {
        mUserInterface = userInterface;
        mServices = services;
    }

    public void start() throws LaunchException {
        mUserInterface.launch();
        mServices.forEach(Service::start);
    }

    public void stop() {
        mServices.forEach((service)-> {
            if (service.isRunning()) {
                service.stop();
            }
        });
        mUserInterface.shutdown();
    }
}
