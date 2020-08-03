package com.flash3388.frc.ds.configuration;

import com.flash3388.frc.ds.configuration.store.Store;
import com.flash3388.frc.ds.configuration.store.exceptions.NoEntryException;
import com.flash3388.frc.ds.util.ErrorHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final Store mStore;
    private final Logger mLogger;
    private final ErrorHandler mErrorHandler;

    private final Map<String, Object> mDefaultValues;
    private final Map<String, ValueType> mValueTypes;
    private final Map<String, Property<?>> mRetrievedProperties;

    public Configuration(Store store, Logger logger, ErrorHandler errorHandler, Map<String, Object> defaultValues, Map<String, ValueType> valueTypes) {
        mStore = store;
        mLogger = logger;
        mErrorHandler = errorHandler;
        mDefaultValues = defaultValues;
        mValueTypes = valueTypes;

        mRetrievedProperties = new HashMap<>();
    }

    public IntegerProperty getInt(String name) throws IOException {
        return getIntProperty(name);
    }

    public Value getValue(String name, ValueType type) throws IOException {
        return getValue(name, type, true);
    }

    public Value getValue(String name, ValueType type, boolean useDefaults) throws IOException {
        ValueType expectedType = mValueTypes.get(name);
        if (expectedType == null) {
            throw new IOException("missing type definition for " + name);
        }
        if (expectedType != type) {
            throw new IOException(String.format("type mismatch: %s, %s", expectedType, type));
        }

        try {
            Object value = mStore.get(name, type);
            if (!expectedType.isInstance(value)) {
                throw new IOException(String.format("defined value (%s) does not match type (%s)", value, expectedType));
            }

            return new Value(value, expectedType);
        } catch (NoEntryException e) {
            if (useDefaults && mDefaultValues.containsKey(name)) {
                Object value = mDefaultValues.get(name);
                mLogger.warn("No config entry for key {}, using default: {}", name, value, e);

                return new Value(value, expectedType);
            }

            mLogger.warn("No config entry for key {}. No default value found", name);
            throw e;
        } catch (IOException e) {
            throw new IOException(name, e);
        }
    }

    public void putValue(String name, Value value) throws IOException {
        try {
            mStore.put(name, value.getValue());
        } catch (IOException e) {
            throw new IOException(name, e);
        }
    }

    public void putDefaultValues() throws IOException {
        for (Map.Entry<String, Object> entry : mDefaultValues.entrySet()) {
            String name = entry.getKey();
            ValueType type = mValueTypes.get(name);
            if (type == null) {
                throw new IOException("missing type definition for " + name);
            }

            try {
                getValue(name, type, false);
            } catch (NoEntryException e) {
                putValue(name, new Value(entry.getValue(), type));
            }
        }
    }

    public Map<String, String> getAllValuesAsString() throws IOException {
        return mStore.getAllLeafs();
    }

    private <T> Property<T> getProperty(String name, ValueType type) throws IOException {
        if (mRetrievedProperties.containsKey(name)) {
            return (Property<T>) mRetrievedProperties.get(name);
        }

        Value value = getValue(name, type);
        Property<?> property = type.newProperty(this, name, value, mErrorHandler);
        mRetrievedProperties.put(name, property);

        return (Property<T>) property;
    }

    private IntegerProperty getIntProperty(String name) throws IOException {
        Property<?> property = getProperty(name, ValueType.INT);
        if (property instanceof IntegerProperty) {
            return (IntegerProperty) property;
        }

        throw new AssertionError("Error in type of property " + property);
    }
}
