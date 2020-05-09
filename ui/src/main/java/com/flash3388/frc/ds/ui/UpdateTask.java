package com.flash3388.frc.ds.ui;

import com.castle.time.Clock;
import com.castle.time.Time;
import javafx.application.Platform;

import java.util.concurrent.atomic.AtomicReference;

public class UpdateTask implements Runnable {

    private final MainWindow mMainWindow;
    private final Clock mClock;

    private final AtomicReference<Time> mLastTimestamp;

    public UpdateTask(MainWindow mainWindow, Clock clock) {
        mMainWindow = mainWindow;
        mClock = clock;

        mLastTimestamp = new AtomicReference<>(mClock.currentTime());
    }

    @Override
    public void run() {
        Platform.runLater(()->{
            Time now = mClock.currentTime();
            Time deltaTime = now.sub(mLastTimestamp.getAndSet(now));

            mMainWindow.update(deltaTime);
        });
    }
}
