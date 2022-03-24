package com.flash3388.frc.ds.computer;

import com.castle.time.Time;
import com.flash3388.frc.ds.comp.BatteryState;
import com.flash3388.frc.ds.comp.ComputerStatus;
import com.flash3388.frc.ds.comp.ChargeState;
import com.flash3388.frc.ds.comp.NativeException;
import com.flash3388.frc.ds.computer.hid.Joystick;
import com.flash3388.frc.ds.computer.hid.JoystickImpl;
import com.flash3388.frc.ds.computer.hid.SdlData;
import sdl2.PowerInfo;
import sdl2.PowerState;
import sdl2.SDL;
import sdl2.SDLEvents;
import sdl2.SDLJoystick;
import sdl2.SDLPower;
import sdl2.events.Event;
import sdl2.events.JAxisMotionEvent;
import sdl2.events.JButtonChangeEvent;
import sdl2.events.JDeviceConnectionEvent;
import sdl2.events.JHatMotionEvent;

import java.util.function.Supplier;

public class UpdateTask implements Runnable {

    private final ComputerStatusContainer mComputerStatusContainer;
    private final Time mRunInterval;

    private int mNextIndex;

    public UpdateTask(ComputerStatusContainer computerStatusContainer, Time runInterval) {
        mComputerStatusContainer = computerStatusContainer;
        mRunInterval = runInterval;
    }

    @Override
    public void run() {
        mNextIndex = 0;
        SDL.init(SDL.INIT_JOYSTICK | SDL.INIT_EVENTS);
        try {
            while (!Thread.interrupted()) {
                pollUpdate();

                try {
                    Thread.sleep((long) mRunInterval.valueAsMillis());
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            SDL.quit();
        }
    }

    private void pollUpdate() {
        try {
            PowerInfo powerInfo = SDLPower.getPowerInfo();
            mComputerStatusContainer.levelProperty().set(powerInfo.getPercentage() / 100.0);
            mComputerStatusContainer.isChargingProperty().set(
                    powerInfo.getState() == PowerState.CHARGING ||
                            powerInfo.getState() == PowerState.CHARGING);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        Event event = SDLEvents.pollEvent();
        while (event != null) {
            handleSdlEvent(event);
            event = SDLEvents.pollEvent();
        }
    }

    private void handleSdlEvent(Event event) {
        switch (event.getType()) {
            case JOY_AXIS_MOTION: {
                JAxisMotionEvent axisMotionEvent = (JAxisMotionEvent) event;
                Joystick joystick = mComputerStatusContainer.getJoystickForId(axisMotionEvent.getJoystickId());
                joystick.setAxisValue(axisMotionEvent.getAxis(), SdlData.convertAxisValue(axisMotionEvent.getValue()));
                break;
            }
            case JOY_BUTTON_UP:
            case JOY_BUTTON_DOWN: {
                JButtonChangeEvent buttonEvent = (JButtonChangeEvent) event;
                Joystick joystick = mComputerStatusContainer.getJoystickForId(buttonEvent.getJoystickId());
                joystick.setButtonValue(buttonEvent.getButton(), buttonEvent.getState());
                break;
            }
            case JOY_HAT_MOTION: {
                JHatMotionEvent hatMotionEvent = (JHatMotionEvent) event;
                Joystick joystick = mComputerStatusContainer.getJoystickForId(hatMotionEvent.getJoystickId());
                joystick.setHatValue(hatMotionEvent.getHat(), SdlData.convertHatValue(hatMotionEvent.getValue()));
                break;
            }
            case JOY_DEVICE_ADDED:
            case JOY_DEVICE_REMOVED:
                JDeviceConnectionEvent connectionEvent = (JDeviceConnectionEvent) event;
                if (connectionEvent.isConnected()) {
                    long ptr = SDLJoystick.open(connectionEvent.getWhich());
                    Joystick joystick = new JoystickImpl(ptr);
                    mComputerStatusContainer.addJoystick(joystick);
                    mComputerStatusContainer.fireJoystickConnectionEvent(joystick, true);
                } else {
                    Joystick joystick = mComputerStatusContainer.getJoystickForId(connectionEvent.getWhich());
                    mComputerStatusContainer.removeJoystick(joystick);
                    mComputerStatusContainer.fireJoystickConnectionEvent(joystick, false);
                }
                break;
        }
    }
}
