package com.flash3388.frc.ds.robot;

import javafx.beans.property.ReadOnlyDoubleProperty;

public interface RobotPowerStatus {

    ReadOnlyDoubleProperty voltageProperty();
    ReadOnlyDoubleProperty maxVoltageProperty();
}
