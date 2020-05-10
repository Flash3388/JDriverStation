package com.flash3388.frc.ds.api;

public enum DsTeamAlliance {
    BLUE(0),
    RED(1)
    ;

    private final int mValue;

    DsTeamAlliance(int value) {
        mValue = value;
    }

    public int value() {
        return mValue;
    }

    public static DsTeamAlliance fromValue(int value) {
        for (DsTeamAlliance teamAlliance : values()) {
            if (teamAlliance.value() == value) {
                return teamAlliance;
            }
        }

        throw new EnumConstantNotPresentException(DsTeamAlliance.class, String.valueOf(value));
    }
}
