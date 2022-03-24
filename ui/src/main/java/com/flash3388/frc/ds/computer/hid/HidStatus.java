package com.flash3388.frc.ds.computer.hid;

import java.util.List;

public interface HidStatus {

    List<Joystick> getJoysticks();
    Joystick getJoystickForIndex(int index);
    Joystick getJoystickForId(int id);
    void reassignJoystickIndex(int source, int destination);

    void addJoystickConnectionListener(JoystickConnectionListener listener);
}
