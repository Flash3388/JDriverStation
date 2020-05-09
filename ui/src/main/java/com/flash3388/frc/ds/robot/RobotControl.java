package com.flash3388.frc.ds.robot;

public interface RobotControl {

    void setEnabled(boolean enabled);
    void setControlMode(RobotControlMode controlMode);

    void rebootRoboRio();
    void restartCode();
}
