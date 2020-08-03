package com.flash3388.frc.ds.robot.beans;

import com.flash3388.frc.ds.api.DriverStation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TeamNumberProperty extends SimpleIntegerProperty {

    private final IntegerProperty mConfigurationProperty;
    private final DriverStation mDriverStation;

    public TeamNumberProperty(IntegerProperty configurationProperty, DriverStation driverStation) {
        super(configurationProperty.get());
        mConfigurationProperty = configurationProperty;
        mDriverStation = driverStation;

        mDriverStation.setTeamNumber(configurationProperty.get());
    }

    @Override
    public void set(int newValue) {
        int last = get();
        if (last == newValue) {
            return;
        }

        super.set(newValue);

        if (newValue == get()) {
            mDriverStation.setTeamNumber(newValue);
            mConfigurationProperty.set(newValue);
        }
    }
}
