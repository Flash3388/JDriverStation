package com.flash3388.frc.ds.robot;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

public interface RobotControl {

    void setEnabled(boolean enabled);
    void setControlMode(RobotControlMode controlMode);

    ObservableBooleanValue enabledProperty();
    ObservableValue<RobotControlMode> controlModeProperty();

    void rebootRoboRio();
    void restartCode();
}
