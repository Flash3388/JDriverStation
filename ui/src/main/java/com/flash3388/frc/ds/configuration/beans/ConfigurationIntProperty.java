package com.flash3388.frc.ds.configuration.beans;

import com.flash3388.frc.ds.configuration.Configuration;
import com.flash3388.frc.ds.configuration.Value;
import com.flash3388.frc.ds.configuration.ValueType;
import com.flash3388.frc.ds.util.ErrorHandler;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

public class ConfigurationIntProperty extends SimpleIntegerProperty {

    private final Configuration mConfiguration;
    private final String mName;
    private final ErrorHandler mThrowableHandler;

    public ConfigurationIntProperty(Configuration configuration, String name, Value value, ErrorHandler throwableHandler) {
        super(ValueType.INT.cast(value.getValue()));
        mConfiguration = configuration;
        mName = name;
        mThrowableHandler = throwableHandler;
    }

    @Override
    public void set(int newValue) {
        super.set(newValue);

        if (getValue() == newValue) {
            Value configValue = new Value(newValue, ValueType.INT);

            try {
                mConfiguration.putValue(mName, configValue);
            } catch (IOException e) {
                mThrowableHandler.handle(String.format("Failed to store value %s", configValue), e);
            }
        }
    }
}
