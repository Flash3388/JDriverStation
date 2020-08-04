package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DsProtocol;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

public interface DriverStationControl {

    ObservableBooleanValue fmsConnectedProperty();
    ObservableBooleanValue robotConnectedProperty();
    ObservableBooleanValue radioConnectedProperty();
    ObservableBooleanValue robotHasCodeProperty();

    void setEnabled(boolean enabled);
    void setControlMode(RobotControlMode controlMode);
    ObservableBooleanValue enabledProperty();
    ObservableValue<RobotControlMode> controlModeProperty();
    void rebootRobot();
    void restartCode();

    ReadOnlyDoubleProperty voltageProperty();
    ReadOnlyDoubleProperty maxVoltageProperty();

    IntegerProperty teamNumberProperty();
    Property<TeamStation> teamStationProperty();
    Property<DsProtocol> protocolProperty();

    ReadOnlyDoubleProperty cpuUsageProperty();
    ReadOnlyDoubleProperty ramUsageProperty();
    ReadOnlyDoubleProperty diskUsageProperty();
    ReadOnlyDoubleProperty canUtilizationProperty();

    boolean canEnableRobot();
}
