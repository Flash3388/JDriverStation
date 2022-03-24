package com.flash3388.frc.ds.computer;

import com.flash3388.frc.ds.api.DriverStationJNI;
import com.flash3388.frc.ds.computer.hid.HidStatus;
import com.flash3388.frc.ds.computer.hid.Joystick;
import com.flash3388.frc.ds.computer.hid.JoystickConnectionChangeEvent;
import com.flash3388.frc.ds.computer.hid.JoystickConnectionListener;
import com.flash3388.frc.ds.computer.hid.JoystickImpl;
import com.notifier.EventController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import sdl2.SDL;
import sdl2.SDLJoystick;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ComputerStatusContainer implements BatteryStatus, CpuStatus, HidStatus {

    private final EventController mEventController;

    private final DoubleProperty mBatteryLevelProperty;
    private final BooleanProperty mBatteryChargingProperty;
    private final DoubleProperty mCpuUsageProperty;

    private final Set<Joystick> mJoysticks;

    public ComputerStatusContainer(EventController eventController,
                                   DoubleProperty batteryLevelProperty,
                                   BooleanProperty batteryChargingProperty,
                                   DoubleProperty cpuUsageProperty) {
        mEventController = eventController;
        mBatteryLevelProperty = batteryLevelProperty;
        mBatteryChargingProperty = batteryChargingProperty;
        mCpuUsageProperty = cpuUsageProperty;
        mJoysticks = new HashSet<>();
    }

    public ComputerStatusContainer(EventController eventController) {
        this(eventController, new SimpleDoubleProperty(), new SimpleBooleanProperty(), new SimpleDoubleProperty());
    }

    @Override
    public DoubleProperty levelProperty() {
        return mBatteryLevelProperty;
    }

    @Override
    public BooleanProperty isChargingProperty() {
        return mBatteryChargingProperty;
    }

    @Override
    public DoubleProperty usageProperty() {
        return mCpuUsageProperty;
    }

    @Override
    public List<Joystick> getJoysticks() {
        return new ArrayList<>(mJoysticks);
    }

    @Override
    public Joystick getJoystickForIndex(int index) {
        for (Joystick joystick : mJoysticks) {
            if (joystick.indexProperty().get() == index) {
                return joystick;
            }
        }

        return null;
    }

    @Override
    public Joystick getJoystickForId(int id) {
        for (Joystick joystick : mJoysticks) {
            if (joystick.getId() == id) {
                return joystick;
            }
        }

        return null;
    }

    @Override
    public void reassignJoystickIndex(int source, int destination) {
        Joystick joystick = getJoystickForIndex(source);
        Joystick joystickAtDest = getJoystickForIndex(destination);

        joystick.indexProperty().set(destination);
        if (joystickAtDest != null) {
            joystickAtDest.indexProperty().set(source);
        }

        List<Joystick> joysticks = getJoysticks();
        joysticks.sort(Comparator.comparingInt((j)-> j.indexProperty().get()));

        DriverStationJNI.resetJoysticks();
        joysticks.forEach((j)->  DriverStationJNI.addJoystick(j.getAxisCount(), j.getButtonCount(), j.getHatCount()));
    }

    @Override
    public void addJoystickConnectionListener(JoystickConnectionListener listener) {
        mEventController.registerListener(listener);
    }

    public void addJoystick(Joystick joystick) {
        joystick.indexProperty().set(findFreeIndex());
        mJoysticks.add(joystick);
        DriverStationJNI.addJoystick(joystick.getAxisCount(), joystick.getButtonCount(), joystick.getHatCount());
    }

    public void removeJoystick(Joystick joystick) {
        mJoysticks.remove(joystick);
    }

    public void fireJoystickConnectionEvent(Joystick joystick, boolean connected) {
        mEventController.fire(
                new JoystickConnectionChangeEvent(joystick, connected),
                JoystickConnectionChangeEvent.class,
                JoystickConnectionListener.class,
                JoystickConnectionListener::onConnectionChange);
    }

    private int findFreeIndex() {
        List<Integer> joysticks = getJoysticks().stream()
                .map((j)-> j.indexProperty().get())
                .collect(Collectors.toList());

        for (int i = 0; i < 6; i++) {
            if (!joysticks.contains(i)) {
                return i;
            }
        }

        return -1;
    }
}
