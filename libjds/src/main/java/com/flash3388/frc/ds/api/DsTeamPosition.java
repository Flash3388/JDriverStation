package com.flash3388.frc.ds.api;

public enum DsTeamPosition {
    POSITION_1(0, 1),
    POSITION_2(1, 2),
    POSITION_3(2, 3)
    ;

    private final int mValue;
    private final int mNumber;

    DsTeamPosition(int value, int number) {
        mValue = value;
        mNumber = number;
    }

    public int value() {
        return mValue;
    }

    public int number() {
        return mNumber;
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
