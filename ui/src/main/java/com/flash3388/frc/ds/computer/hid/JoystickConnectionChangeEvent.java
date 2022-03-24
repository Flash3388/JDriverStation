package com.flash3388.frc.ds.computer.hid;

import com.notifier.Event;

public class JoystickConnectionChangeEvent implements Event {

    private final Joystick mJoystick;
    private final boolean mIsConnected;

    public JoystickConnectionChangeEvent(Joystick joystick, boolean isConnected) {
        mJoystick = joystick;
        mIsConnected = isConnected;
    }

    public Joystick getJoystick() {
        return mJoystick;
    }

    public boolean isConnected() {
        return mIsConnected;
    }
}
