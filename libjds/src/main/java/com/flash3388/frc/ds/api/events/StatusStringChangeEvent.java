package com.flash3388.frc.ds.api.events;

public class StatusStringChangeEvent extends DsEvent {

    private final String mStatus;

    public StatusStringChangeEvent(String status) {
        super(DsEventType.STATUS_STRING_CHANGED);
        mStatus = status;
    }

    public String getStatus() {
        return mStatus;
    }
}
