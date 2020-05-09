#pragma once

enum battery_powerstate {
    POWER_CHARGING = 0,
    POWER_CHARGED = 1,
    POWER_DISCHARGING = 2
};

struct battery_state {
    enum battery_powerstate powerstate;
    float chargelevel;
};

int battery_get_state(struct battery_state*);


/*WINDOWS
SYSTEM_POWER_STATUS status;
if(GetSystemPowerStatus(&status)) {
    unsigned char battery = status.BatteryLifePercent;
    // battery := 0..100 or 255 if unknown
    if(battery == 255) {
        printf("Battery level unknown !");
    }
    else {
        printf("Battery level : %u%%.", battery);
    }
}
else {
    printf("Cannot get the power status, error %lu", GetLastError());
}*/