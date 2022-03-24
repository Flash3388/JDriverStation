package com.flash3388.frc.ds.api;

import com.flash3388.frc.ds.api.events.DsEvent;

public class DriverStationJNI {

    private DriverStationJNI() {}

    public static native int isInitialized();
    public static native int initialize();
    public static native void close();

    public static native int rebootRobot();
    public static native int restartRobotCode();

    public static native int setRobotEnabled(boolean enabled);
    public static native boolean isRobotEnabled();
    public static native int setControlMode(int controlMode);
    public static native int getControlMode();

    public static native int isEmergencyStopped();
    public static native int setEmergencyStopped(boolean enabled);

    public static native int configureProtocol(int protocol);

    public static native boolean hasRobotCode();
    public static native boolean isConnectedToFms();
    public static native boolean isConnectedToRobot();
    public static native boolean isConnectedToRadio();
    public static native int getCpuUsage();
    public static native int getRamUsage();
    public static native int getDiskUsage();
    public static native int getCanUsage();
    public static native float getRobotVoltage();
    public static native float getMaximumRobotVoltage();

    public static native String getStatusString();

    public static native int setTeamNumber(int teamNumber);
    public static native int getTeamNumber();
    public static native int setTeamAlliance(int teamAlliance);
    public static native int getTeamAlliance();
    public static native int setTeamPosition(int teamPosition);
    public static native int getTeamPosition();

    public static native void resetJoysticks();
    public static native void addJoystick(int axes, int buttons, int hats);
    public static native int getJoystickCount();
    public static native float getJoystickAxis(int index, int axis);
    public static native boolean getJoystickButton(int index, int button);
    public static native int getJoystickHat(int index, int hat);
    public static native void setJoystickAxis(int index, int axis, float value);
    public static native void setJoystickButton(int index, int button, boolean value);
    public static native void setJoystickHat(int index, int hat, int angle);

    public static native DsEvent pollEvent();
}
