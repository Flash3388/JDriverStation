package com.flash3388.frc.ds.api.events;

public class FmsConnectionChangeEvent extends DsEvent {

    private final boolean mIsConnected;

    public FmsConnectionChangeEvent(boolean isConnected) {
        super(DsEventType.FMS_COMMS_CHANGED);
        mIsConnected = isConnected;
    }

    public boolean isConnected() {
        return mIsConnected;
    }
}
