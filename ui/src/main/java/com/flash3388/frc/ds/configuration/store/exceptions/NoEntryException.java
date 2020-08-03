package com.flash3388.frc.ds.configuration.store.exceptions;

import java.io.IOException;

public class NoEntryException extends IOException {

    public NoEntryException(String name) {
        super(name);
    }
}
