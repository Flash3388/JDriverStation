package com.flash3388.frc.ds.api.events;

public class JoystickCountChangeEvent extends DsEvent {

    private final int mCount;

    public JoystickCountChangeEvent(int count) {
        super(DsEventType.JOYSTICK_COUNT_CHANGED);
        mCount = count;
    }

    public int getCount() {
        return mCount;
    }
}
