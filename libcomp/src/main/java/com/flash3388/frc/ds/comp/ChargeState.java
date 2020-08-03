package com.flash3388.frc.ds.comp;

public enum ChargeState {
    POWER_CHARGING(0),
    POWER_CHARGED(1),
    POWER_DISCHARGING(2)
    ;

    private final int mValue;

    ChargeState(int value) {
        mValue = value;
    }

    public int value() {
        return mValue;
    }

    public static ChargeState fromValue(int value) {
        for (ChargeState state : ChargeState.values()) {
            if (state.value() == value) {
                return state;
            }
        }

        throw new EnumConstantNotPresentException(ChargeState.class,
                String.valueOf(value));
    }
}
