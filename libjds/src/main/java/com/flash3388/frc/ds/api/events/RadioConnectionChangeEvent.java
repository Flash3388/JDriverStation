package com.flash3388.frc.ds.api.events;

public class RadioConnectionChangeEvent extends DsEvent {

    private final boolean mIsConnected;

    public RadioConnectionChangeEvent(boolean isConnected) {
        super(DsEventType.RADIO_COMMS_CHANGED);
        mIsConnected = isConnected;
    }

    public boolean isConnected() {
        return mIsConnected;
    }
}
