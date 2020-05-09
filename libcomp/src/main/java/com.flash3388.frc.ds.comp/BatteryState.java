package com.flash3388.frc.ds.comp;

public class BatteryState {

    private final double mChargeLevel;
    private final ChargeState mChargeState;

    public BatteryState(double chargeLevel, ChargeState chargeState) {
        mChargeLevel = chargeLevel;
        mChargeState = chargeState;
    }

    public double getChargeLevel() {
        return mChargeLevel;
    }

    public ChargeState getChargeState() {
        return mChargeState;
    }

    @Override
    public String toString() {
        return String.format("Level %.3f, %s", getChargeLevel(), getChargeState());
    }
}
