package com.flash3388.frc.ds.api.events;

public class DsEvent {

    private final DsEventType mType;

    public DsEvent(DsEventType type) {
        mType = type;
    }

    public DsEventType getType() {
        return mType;
    }
}
