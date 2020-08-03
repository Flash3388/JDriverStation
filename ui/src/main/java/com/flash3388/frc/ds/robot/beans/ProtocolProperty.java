package com.flash3388.frc.ds.robot.beans;

import com.flash3388.frc.ds.api.DriverStation;
import com.flash3388.frc.ds.api.DsProtocol;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ProtocolProperty extends SimpleObjectProperty<DsProtocol> {

    private final IntegerProperty mConfigurationProperty;
    private final DriverStation mDriverStation;

    public ProtocolProperty(IntegerProperty configurationProperty, DriverStation driverStation) {
        super(DsProtocol.fromValue(configurationProperty.get()));
        mConfigurationProperty = configurationProperty;
        mDriverStation = driverStation;

        mDriverStation.configureProtocol(DsProtocol.fromValue(configurationProperty.get()));
    }

    @Override
    public void set(DsProtocol newValue) {
        DsProtocol last = get();
        if (last.equals(newValue)) {
            return;
        }

        super.set(newValue);

        if (newValue.equals(get())) {
            mDriverStation.configureProtocol(newValue);
            mConfigurationProperty.set(newValue.value());
        }
    }
}
