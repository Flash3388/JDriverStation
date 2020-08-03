package com.flash3388.frc.ds.robot;

import com.flash3388.frc.ds.api.DsTeamAlliance;
import com.flash3388.frc.ds.api.DsTeamPosition;

import java.util.Arrays;
import java.util.Collection;

public class TeamStation {

    private static Collection<TeamStation> sTeamStations = Arrays.asList(
            new TeamStation(DsTeamAlliance.RED, DsTeamPosition.POSITION_1),
            new TeamStation(DsTeamAlliance.RED, DsTeamPosition.POSITION_2),
            new TeamStation(DsTeamAlliance.RED, DsTeamPosition.POSITION_3),
            new TeamStation(DsTeamAlliance.BLUE, DsTeamPosition.POSITION_1),
            new TeamStation(DsTeamAlliance.BLUE, DsTeamPosition.POSITION_2),
            new TeamStation(DsTeamAlliance.BLUE, DsTeamPosition.POSITION_3)
    );

    private final DsTeamAlliance mTeamAlliance;
    private final DsTeamPosition mTeamPosition;

    private TeamStation(DsTeamAlliance teamAlliance, DsTeamPosition teamPosition) {
        mTeamAlliance = teamAlliance;
        mTeamPosition = teamPosition;
    }

    public DsTeamAlliance getTeamAlliance() {
        return mTeamAlliance;
    }

    public DsTeamPosition getTeamPosition() {
        return mTeamPosition;
    }

    @Override
    public String toString() {
        return String.format("%s %d", getTeamAlliance().name(), getTeamPosition().number());
    }

    public static Collection<TeamStation> values() {
        return sTeamStations;
    }

    public static TeamStation defaultValue() {
        return values().iterator().next();
    }

    public static TeamStation fromDriverStationData(DsTeamAlliance teamAlliance, DsTeamPosition teamPosition) {
        for (TeamStation teamStation : values()) {
            if (teamAlliance == teamStation.getTeamAlliance() &&
                    teamPosition == teamStation.getTeamPosition()) {
                return teamStation;
            }
        }

        throw new IllegalArgumentException(String.format("Unknown Team Station %s %s", teamAlliance, teamPosition));
    }
}
