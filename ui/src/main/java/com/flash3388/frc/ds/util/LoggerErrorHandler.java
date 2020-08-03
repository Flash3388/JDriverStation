package com.flash3388.frc.ds.util;

import org.slf4j.Logger;

public class LoggerErrorHandler implements ErrorHandler {

    private final Logger mLogger;

    public LoggerErrorHandler(Logger logger) {
        mLogger = logger;
    }

    @Override
    public void handle(String message, Throwable throwable) {
        mLogger.error(message, throwable);
    }
}
