package com.flash3388.frc.ds.configuration.beans;

import com.castle.util.throwables.ThrowableHandler;
import com.flash3388.frc.ds.configuration.Configuration;
import com.flash3388.frc.ds.configuration.Value;
import com.flash3388.frc.ds.configuration.ValueType;
import com.flash3388.frc.ds.util.ErrorHandler;
import javafx.beans.property.SimpleObjectProperty;

import java.io.IOException;

public class ConfigurationProperty<T> extends SimpleObjectProperty<T> {

    private final Configuration mConfiguration;
    private final String mName;
    private final ValueType mValueType;
    private final ErrorHandler mThrowableHandler;

    public ConfigurationProperty(Configuration configuration, String name, Value value, ValueType valueType, ErrorHandler throwableHandler) {
        super(valueType.cast(value.getValue()));
        mConfiguration = configuration;
        mName = name;
        mValueType = valueType;
        mThrowableHandler = throwableHandler;
    }

    @Override
    public void set(T value) {
        super.set(value);

        if (getValue() == value) {
            Value configValue = new Value(value, mValueType);

            try {
                mConfiguration.putValue(mName, configValue);
            } catch (IOException e) {
                mThrowableHandler.handle(String.format("Failed to store value %s", configValue), e);
            }
        }
    }
}
