package com.flash3388.frc.ds.configuration;

import com.flash3388.frc.ds.api.DsProtocol;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationValues {

    public Map<String, Value> load() {
        Map<String, Value> valueMap = new HashMap<>();

        valueMap.put(ConfigurationKeys.TEAM_NUMBER, new Value(3388, ValueType.INT));
        valueMap.put(ConfigurationKeys.COMM_PROTOCOL, new Value(DsProtocol.FRC2016.value(), ValueType.INT));
        return valueMap;
    }
}
