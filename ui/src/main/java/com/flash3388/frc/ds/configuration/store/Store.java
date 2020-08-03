package com.flash3388.frc.ds.configuration.store;

import com.flash3388.frc.ds.configuration.ValueType;

import java.io.IOException;
import java.util.Map;

public interface Store {

    void put(String name, Object value) throws IOException;
    Object get(String name, ValueType type) throws IOException;
    Map<String, String> getAllLeafs() throws IOException;
}
