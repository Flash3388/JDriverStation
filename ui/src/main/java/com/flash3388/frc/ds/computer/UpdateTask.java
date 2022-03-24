package com.flash3388.frc.ds.computer;

import com.castle.time.Time;
import com.flash3388.frc.ds.comp.BatteryState;
import com.flash3388.frc.ds.comp.ComputerStatus;
import com.flash3388.frc.ds.comp.ChargeState;
import com.flash3388.frc.ds.comp.NativeException;
import sdl2.PowerInfo;
import sdl2.PowerState;
import sdl2.SDL;
import sdl2.SDLEvents;
import sdl2.SDLPower;
import sdl2.events.Event;

import java.util.function.Supplier;

public class UpdateTask implements Runnable {

    private final ComputerStatusContainer mComputerStatusContainer;
    private final Time mRunInterval;

    public UpdateTask(ComputerStatusContainer computerStatusContainer, Time runInterval) {
        mComputerStatusContainer = computerStatusContainer;
        mRunInterval = runInterval;
    }

    @Override
    public void run() {
        SDL.init(SDL.INIT_JOYSTICK);
        try {
            while (!Thread.interrupted()) {
                pollUpdate();

                try {
                    Thread.sleep((long) mRunInterval.valueAsMillis());
                } catch (InterruptedException e) {
                    break;
                }
            }
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
    }
}
