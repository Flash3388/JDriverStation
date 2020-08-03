package com.flash3388.frc.ds.api;

public class DriverStation implements AutoCloseable {

    private static DriverStation sInstance;

    private DriverStation() {}

    public static synchronized DriverStation getInstance() {
        if (sInstance == null) {
            DriverStationJNI.initialize();
            sInstance = new DriverStation();
        }

        return sInstance;
    }

    public void rebootRobot() {
        int result = DriverStationJNI.rebootRobot();
        checkError(result);
    }
    public void restartRobotCode() {
        int result = DriverStationJNI.restartRobotCode();
        checkError(result);
    }

    public void setRobotEnabled(boolean enabled) {
        int result = DriverStationJNI.setRobotEnabled(enabled);
        checkError(result);
    }
    public boolean isRobotEnabled() {
        int result = DriverStationJNI.isRobotEnabled();
        checkError(result);

        return result == 1;
    }
    public void setControlMode(DsControlMode controlMode) {
        int result = DriverStationJNI.setControlMode(controlMode.value());
        checkError(result);
    }
    public DsControlMode getControlMode() {
        int result = DriverStationJNI.getControlMode();
        checkError(result);

        return DsControlMode.fromValue(result);
    }

    public boolean isEmergencyStopped() {
        int result = DriverStationJNI.isEmergencyStopped();
        checkError(result);

        return result == 1;
    }
    public void setEmergencyStopped(boolean enabled) {
        int result = DriverStationJNI.setEmergencyStopped(enabled);
        checkError(result);
    }

    public void configureProtocol(DsProtocol protocol) {
        int result = DriverStationJNI.configureProtocol(protocol.value());
        checkError(result);
    }

    public boolean hasRobotCode() {
        int result = DriverStationJNI.hasRobotCode();
        checkError(result);

        return result == 1;
    }
    public boolean isConnectedToFms() {
        int result = DriverStationJNI.isConnectedToFms();
        checkError(result);

        return result == 1;
    }
    public boolean isConnectedToRobot() {
        int result = DriverStationJNI.isConnectedToRobot();
        checkError(result);

        return result == 1;
    }
    public boolean isConnectedToRadio() {
        int result = DriverStationJNI.isConnectedToRadio();
        checkError(result);

        return result == 1;
    }
    public int getCpuUsage() {
        int result = DriverStationJNI.getCpuUsage();
        checkError(result);

        return result;
    }
    public int getRamUsage() {
        int result = DriverStationJNI.getRamUsage();
        checkError(result);

        return result;
    }
    public int getDiskUsage() {
        int result = DriverStationJNI.getDiskUsage();
        checkError(result);

        return result;
    }
    public int getCanUsage() {
        int result = DriverStationJNI.getCanUsageUsage();
        checkError(result);

        return result;
    }
    public float getRobotVoltage() {
        float result = DriverStationJNI.getRobotVoltage();
        checkError((int) result);

        return result;
    }
    public float getMaximumRobotVoltage() {
        float result = DriverStationJNI.getMaximumRobotVoltage();
        checkError((int) result);

        return result;
    }

    public String getStatusString() {
        return DriverStationJNI.getStatusString();
    }

    public void setTeamNumber(int teamNumber) {
        int result = DriverStationJNI.setTeamNumber(teamNumber);
        checkError(result);
    }
    public int getTeamNumber() {
        int result = DriverStationJNI.getTeamNumber();
        checkError(result);

        return result;
    }
    public void setTeamAlliance(DsTeamAlliance teamAlliance) {
        int result = DriverStationJNI.setTeamAlliance(teamAlliance.value());
        checkError(result);
    }
    public DsTeamAlliance getTeamAlliance() {
        int result = DriverStationJNI.getTeamAlliance();
        checkError(result);

        return DsTeamAlliance.fromValue(result);
    }
    public void setTeamPosition(DsTeamPosition teamPosition) {
        int result = DriverStationJNI.setTeamPosition(teamPosition.value());
        checkError(result);
    }
    public DsTeamPosition getTeamPosition() {
        int result = DriverStationJNI.getTeamPosition();
        checkError(result);

        return DsTeamPosition.fromValue(result);
    }

    public int getJoystickCount() {
        int result = DriverStationJNI.getJoystickCount();
        checkError(result);

        return result;
    }
    
    @Override
    public void close() {
        DriverStationJNI.close();
    }

    private void checkError(int result) {
        if (result < 0) {
            throw new NativeException(result);
        }
    }
}
