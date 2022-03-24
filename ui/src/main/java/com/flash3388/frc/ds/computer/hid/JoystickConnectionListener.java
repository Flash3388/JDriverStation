package com.flash3388.frc.ds.computer.hid;

import com.notifier.Listener;

@FunctionalInterface
public interface JoystickConnectionListener extends Listener {

    void onConnectionChange(JoystickConnectionChangeEvent event);
}
