package com.flash3388.frc.ds.robot.beans;

import com.flash3388.frc.ds.api.DriverStation;
import com.flash3388.frc.ds.robot.TeamStation;
import javafx.beans.property.SimpleObjectProperty;

public class TeamStationProperty extends SimpleObjectProperty<TeamStation> {

    private final DriverStation mDriverStation;

    public TeamStationProperty(DriverStation driverStation, TeamStation initialValue) {
        super(initialValue);
        mDriverStation = driverStation;

        mDriverStation.setTeamAlliance(initialValue.getTeamAlliance());
        mDriverStation.setTeamPosition(initialValue.getTeamPosition());
    }

    @Override
    public void set(TeamStation newValue) {
        TeamStation last = get();
        if (last.equals(newValue)) {
            return;
        }

        super.set(newValue);

        if (newValue.equals(get())) {
            mDriverStation.setTeamAlliance(newValue.getTeamAlliance());
            mDriverStation.setTeamPosition(newValue.getTeamPosition());
        }
    }
}
