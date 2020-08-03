package com.flash3388.frc.ds.configuration;

import com.flash3388.frc.ds.configuration.beans.ConfigurationIntProperty;
import com.flash3388.frc.ds.util.ErrorHandler;
import javafx.beans.property.Property;

public enum ValueType {
    INT(Integer.class) {
        @Override
        Property<?> newProperty(Configuration configuration, String name, Value value, ErrorHandler errorHandler) {
            return new ConfigurationIntProperty(configuration, name, value, errorHandler);
        }
    }
    ;

    private final Class<?> mClassType;

    ValueType(Class<?> classType) {
        mClassType = classType;
    }

    public boolean isInstance(Object value) {
        return mClassType.isInstance(value);
    }

    public <T> T cast(Object value) {
        return (T) mClassType.cast(value);
    }

    public Class<?> javaClass() {
        return mClassType;
    }

    abstract Property<?> newProperty(Configuration configuration, String name, Value value, ErrorHandler errorHandler);
}
