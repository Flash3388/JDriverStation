package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.util.Strings;

public enum TeamColor {
    RED,
    BLUE;

    public String displayName() {
        return Strings.capitalize(name().toLowerCase());
    }
}
