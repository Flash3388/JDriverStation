package com.flash3388.frc.ds.computer.hid;

import com.flash3388.frc.ds.api.DriverStationJNI;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import sdl2.SDLJoystick;

public class JoystickImpl implements Joystick {

    private final long mNativePtr;
    private final int mId;
    private final IntegerProperty mIndex;

    public JoystickImpl(long nativePtr) {
        mNativePtr = nativePtr;
        mId = SDLJoystick.getInstanceId(nativePtr);
        mIndex = new SimpleIntegerProperty(-1);
    }

    @Override
    public String getName() {
        return SDLJoystick.getName(mNativePtr);
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public IntegerProperty indexProperty() {
        return mIndex;
    }

    @Override
    public int getAxisCount() {
        return SDLJoystick.getNumAxes(mNativePtr);
    }

    @Override
    public int getButtonCount() {
        return SDLJoystick.getNumButtons(mNativePtr);
    }

    @Override
    public int getHatCount() {
        return SDLJoystick.getNumHats(mNativePtr);
    }

    @Override
    public float getAxisValue(int index) {
        return SdlData.convertAxisValue(SDLJoystick.getAxis(mNativePtr, index));
    }

    @Override
    public boolean getButtonValue(int index) {
        return SDLJoystick.getButton(mNativePtr, index);
    }

    @Override
    public int getHatValue(int index) {
        return SdlData.convertHatValue(SDLJoystick.getHat(mNativePtr, index));
    }

    @Override
    public void setAxisValue(int index, float value) {
        DriverStationJNI.setJoystickAxis(mIndex.get(), index, value);
    }

    @Override
    public void setButtonValue(int index, boolean value) {
        DriverStationJNI.setJoystickButton(mIndex.get(), index, value);
    }

    @Override
    public void setHatValue(int index, int angle) {
        DriverStationJNI.setJoystickHat(mIndex.get(), index, angle);
    }
}
