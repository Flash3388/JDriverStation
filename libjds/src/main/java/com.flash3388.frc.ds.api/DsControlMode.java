package com.flash3388.frc.ds.api;

public enum DsControlMode {
    TELEOPERATED(0),
    AUTONOMOUS(1),
    TEST(2);

    private final int mValue;

    DsControlMode(int value) {
        mValue = value;
    }

    public int value() {
        return mValue;
    }

    public static DsControlMode fromValue(int value) {
        for (DsControlMode controlMode : values()) {
            if (controlMode.value() == value) {
                return controlMode;
            }
        }

        throw new EnumConstantNotPresentException(DsControlMode.class, String.valueOf(value));
    }
}
