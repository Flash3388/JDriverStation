package com.flash3388.frc.ds.computer.hid;

import sdl2.SDLJoystick;

public class SdlData {

    public static float convertAxisValue(int value) {
        return (float) (value / (double) SDLJoystick.AXIS_MAX);
    }

    public static int convertHatValue(int value) {
        if (value == SDLJoystick.HAT_CENTERED) {
            return -1;
        }

        if ((value & SDLJoystick.HAT_UP) != 0) {
            if ((value & SDLJoystick.HAT_RIGHT) != 0) {
                return 45;
            } else if ((value & SDLJoystick.HAT_LEFT) != 0) {
                return 315;
            }
        } else if ((value & SDLJoystick.HAT_DOWN) != 0) {
            if ((value & SDLJoystick.HAT_RIGHT) != 0) {
                return 135;
            } else if ((value & SDLJoystick.HAT_LEFT) != 0) {
                return 225;
            }
        } else if ((value & SDLJoystick.HAT_RIGHT) != 0) {
            return 90;
        } else if ((value & SDLJoystick.HAT_LEFT) != 0) {
            return 270;
        }

        return 0;
    }
}
