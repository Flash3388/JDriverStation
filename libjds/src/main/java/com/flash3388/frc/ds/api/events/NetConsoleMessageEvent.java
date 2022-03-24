package com.flash3388.frc.ds.api.events;

public class NetConsoleMessageEvent extends DsEvent {

    private final String mMessage;

    public NetConsoleMessageEvent(String message) {
        super(DsEventType.NETCONSOLE_NEW_MESSAGE);
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
