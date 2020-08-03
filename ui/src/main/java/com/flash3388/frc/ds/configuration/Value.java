package com.flash3388.frc.ds.configuration;

import com.flash3388.frc.ds.util.reflect.Types;

public class Value {

    private final Object mValue;
    private final ValueType mType;

    public Value(Object value, ValueType type) {
        mValue = value;
        mType = type;
    }

    public Object getValue() {
        return mValue;
    }

    public ValueType getType() {
        return mType;
    }

    public Object castValue() {
        return Types.smartCast(mValue, mType.javaClass());
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", mValue, mType);
    }
}
