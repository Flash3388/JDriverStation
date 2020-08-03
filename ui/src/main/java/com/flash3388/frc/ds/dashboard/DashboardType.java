package com.flash3388.frc.ds.dashboard;

public enum DashboardType {
    NONE,
    SMART_DASHBOARD,
    SFX_DASHBOARD;

    public static DashboardType defaultValue() {
        return NONE;
    }
}
