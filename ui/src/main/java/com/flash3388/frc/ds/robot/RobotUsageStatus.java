package com.flash3388.frc.ds.robot;

import javafx.beans.property.ReadOnlyDoubleProperty;

public interface RobotUsageStatus {

    ReadOnlyDoubleProperty cpuUsageProperty();
    ReadOnlyDoubleProperty ramUsageProperty();
    ReadOnlyDoubleProperty diskUsageProperty();
    ReadOnlyDoubleProperty canUtilizationProperty();
}
