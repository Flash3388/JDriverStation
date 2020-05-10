package com.flash3388.frc.ds.api;

public class NativeException extends RuntimeException {

    private final int mReturnCode;

    public NativeException(int returnCode) {
        super(String.format("Code %d", returnCode));
        mReturnCode = returnCode;
    }

    public int getReturnCode() {
        return mReturnCode;
    }
}
