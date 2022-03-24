package com.flash3388.frc.ds.computer.hid;

import javafx.beans.property.IntegerProperty;

public interface Joystick {

    String getName();
    int getId();
    IntegerProperty indexProperty();

    int getAxisCount();
    int getButtonCount();
    int getHatCount();

    float getAxisValue(int index);
    boolean getButtonValue(int index);
    int getHatValue(int index);

    void setAxisValue(int index, float value);
    void setButtonValue(int index, boolean value);
    void setHatValue(int index, int angle);
}
