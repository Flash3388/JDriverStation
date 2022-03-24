package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DriverStationJNI;
import com.flash3388.frc.ds.api.DsControlMode;
import com.flash3388.frc.ds.api.events.DsEvent;
import com.flash3388.frc.ds.api.events.FmsConnectionChangeEvent;
import com.flash3388.frc.ds.api.events.JoystickCountChangeEvent;
import com.flash3388.frc.ds.api.events.NetConsoleMessageEvent;
import com.flash3388.frc.ds.api.events.RadioConnectionChangeEvent;
import com.flash3388.frc.ds.api.events.RobotStateChangeEvent;
import com.flash3388.frc.ds.api.events.StatusStringChangeEvent;

public class UpdateTask implements Runnable {

    private final DriverStationControlImpl mControl;

    public UpdateTask(DriverStationControlImpl control) {
        mControl = control;
    }

    @Override
    public void run() {
        DriverStationJNI.initialize();
        try {
            setInitialValues();

            while (!Thread.interrupted()) {
                pollEvents();

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            DriverStationJNI.close();
        }
    }

    private void setInitialValues() {
        mControl.joystickCountProperty().set(DriverStationJNI.getJoystickCount());
        mControl.statusStringProperty().set(DriverStationJNI.getStatusString());
        mControl.controlModeProperty().setValue(DsControlMode.fromValue(DriverStationJNI.getControlMode()));
        mControl.robotConnectedProperty().set(DriverStationJNI.isConnectedToRobot());
        mControl.robotHasCodeProperty().set(DriverStationJNI.hasRobotCode());
        mControl.enabledProperty().set(DriverStationJNI.isRobotEnabled());
        mControl.voltageProperty().set(DriverStationJNI.getRobotVoltage());
        mControl.cpuUsageProperty().set(DriverStationJNI.getCpuUsage());
        mControl.diskUsageProperty().set(DriverStationJNI.getDiskUsage());
        mControl.ramUsageProperty().set(DriverStationJNI.getRamUsage());
        mControl.canUtilizationProperty().set(DriverStationJNI.getCanUsage());
        mControl.maxVoltageProperty().set(DriverStationJNI.getMaximumRobotVoltage());
        mControl.fmsConnectedProperty().set(DriverStationJNI.isConnectedToFms());
        mControl.radioConnectedProperty().set(DriverStationJNI.isConnectedToRadio());
    }

    private void pollEvents() {
        DsEvent event = DriverStationJNI.pollEvent();
        if (event == null) {
            return;
        }

        switch (event.getType()) {
            case JOYSTICK_COUNT_CHANGED:
                JoystickCountChangeEvent changeEvent = (JoystickCountChangeEvent) event;
                mControl.joystickCountProperty().set(changeEvent.getCount());
                break;
            case STATUS_STRING_CHANGED:
                StatusStringChangeEvent statusStringChangeEvent = (StatusStringChangeEvent) event;
                mControl.statusStringProperty().set(statusStringChangeEvent.getStatus());
                break;
            case ROBOT_STATE_CHANGED:
                RobotStateChangeEvent robotStateChangeEvent = (RobotStateChangeEvent) event;
                mControl.controlModeProperty().setValue(robotStateChangeEvent.getControlMode());
                mControl.robotConnectedProperty().set(robotStateChangeEvent.isConnected());
                mControl.robotHasCodeProperty().set(robotStateChangeEvent.hasCode());
                mControl.enabledProperty().set(robotStateChangeEvent.isEnabled());
                mControl.voltageProperty().set(robotStateChangeEvent.getVoltage());
                // TODO: estopped
                mControl.cpuUsageProperty().set(robotStateChangeEvent.getCpuUsage());
                mControl.diskUsageProperty().set(robotStateChangeEvent.getDiskUsage());
                mControl.ramUsageProperty().set(robotStateChangeEvent.getRamUsage());
                mControl.canUtilizationProperty().set(robotStateChangeEvent.getCanUtilization());

                mControl.maxVoltageProperty().set(DriverStationJNI.getMaximumRobotVoltage());
                break;
            case FMS_COMMS_CHANGED:
                FmsConnectionChangeEvent fmsConnectionChangeEvent = (FmsConnectionChangeEvent) event;
                mControl.fmsConnectedProperty().set(fmsConnectionChangeEvent.isConnected());
                break;
            case RADIO_COMMS_CHANGED:
                RadioConnectionChangeEvent radioConnectionChangeEvent = (RadioConnectionChangeEvent) event;
                mControl.radioConnectedProperty().set(radioConnectionChangeEvent.isConnected());
                break;
            case NETCONSOLE_NEW_MESSAGE:
                NetConsoleMessageEvent netConsoleMessageEvent = (NetConsoleMessageEvent) event;
                // TODO: MESSAGE
                break;
        }
    }
}
