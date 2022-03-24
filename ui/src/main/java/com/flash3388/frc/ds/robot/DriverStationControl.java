package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DsControlMode;
import com.flash3388.frc.ds.api.DsProtocol;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;

public interface DriverStationControl {

    ReadOnlyBooleanProperty fmsConnectedProperty();
    ReadOnlyBooleanProperty robotConnectedProperty();
    ReadOnlyBooleanProperty radioConnectedProperty();
    ReadOnlyBooleanProperty robotHasCodeProperty();
    ReadOnlyStringProperty statusStringProperty();

    ReadOnlyBooleanProperty enabledProperty();
    ReadOnlyProperty<DsControlMode> controlModeProperty();

    ReadOnlyDoubleProperty voltageProperty();
    ReadOnlyDoubleProperty maxVoltageProperty();

    ReadOnlyIntegerProperty teamNumberProperty();
    ReadOnlyProperty<TeamStation> teamStationProperty();
    ReadOnlyProperty<DsProtocol> protocolProperty();

    ReadOnlyDoubleProperty cpuUsageProperty();
    ReadOnlyDoubleProperty ramUsageProperty();
    ReadOnlyDoubleProperty diskUsageProperty();
    ReadOnlyDoubleProperty canUtilizationProperty();

    ReadOnlyIntegerProperty joystickCountProperty();

    boolean canEnableRobot();

    void rebootRobot();
    void restartCode();
    void setEnabled(boolean enabled);
    void setControlMode(DsControlMode controlMode);
    void setTeamNumber(int teamNumber);
    void setTeamStation(TeamStation teamStation);
    void setProtocol(DsProtocol protocol);
}
