package com.flash3388.frc.ds.api;

public enum DsTeamPosition {
    POSITION_1(0),
    POSITION_2(1),
    POSITION_3(2)
    ;

    private final int mValue;

    DsTeamPosition(int value) {
        mValue = value;
    }

    public int value() {
        return mValue;
    }

    public static DsTeamPosition fromValue(int value) {
        for (DsTeamPosition teamPosition : values()) {
            if (teamPosition.value() == value) {
                return teamPosition;
            }
        }

        throw new EnumConstantNotPresentException(DsTeamPosition.class, String.valueOf(value));
    }
}
