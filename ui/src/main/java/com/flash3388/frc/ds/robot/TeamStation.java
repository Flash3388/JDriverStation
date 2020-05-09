package com.flash3388.frc.ds.robot;

import java.util.Arrays;
import java.util.Collection;

public class TeamStation {

    private static Collection<TeamStation> sTeamStations = Arrays.asList(
            new TeamStation(TeamColor.RED, 1),
            new TeamStation(TeamColor.RED, 2),
            new TeamStation(TeamColor.RED, 3),
            new TeamStation(TeamColor.BLUE, 1),
            new TeamStation(TeamColor.BLUE, 2),
            new TeamStation(TeamColor.BLUE, 3)
    );

    private final TeamColor mTeamColor;
    private final int mNumber;

    public TeamStation(TeamColor teamColor, int number) {
        mTeamColor = teamColor;
        mNumber = number;
    }

    public TeamColor getTeamColor() {
        return mTeamColor;
    }

    public int getNumber() {
        return mNumber;
    }

    @Override
    public String toString() {
        return String.format("%s %d", getTeamColor().displayName(), getNumber());
    }

    public static Collection<TeamStation> getAll() {
        return sTeamStations;
    }
}
