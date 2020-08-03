package com.flash3388.frc.ds.api;

public enum DsProtocol {
    FRC2014(0),
    FRC2015(1),
    FRC2016(2),
    FRC2018(3);

    private final int mValue;

    DsProtocol(int value) {
        mValue = value;
    }

    public int value() {
        return mValue;
    }

    public static DsProtocol defaultValue() {
        return FRC2016;
    }

    public static DsProtocol fromValue(int value) {
        for (DsProtocol protocol : values()) {
            if (protocol.value() == value) {
                return protocol;
            }
        }

        throw new EnumConstantNotPresentException(DsProtocol.class, String.valueOf(value));
    }
}
