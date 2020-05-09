

#include "battery.h"
#include "errors.h"

#ifdef __linux__

#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <dirent.h>
#include <linux/limits.h>
#include <regex.h>
#include <stdbool.h>

#define DATADIR "/sys/class/power_supply"
#define CHARGE_NOW "energy_now"
#define CHARGE_FULL "energy_full"
#define CHARGE_STATUS "status"

#define CHARGE_STATUS_CHARGING "Charging"
#define CHARGE_STATUS_DISCHARGING "Discharging"
#define CHARGE_STATUS_FULL "Full"


static int read_charge_level(const char* data_dir,
                             const char* sub_dir,
                             char* buffer,
                             float* charge_level) {
    FILE* file_current = NULL;
    FILE* file_full = NULL;

    long current;
    long full;

    int result = 0;

    snprintf(buffer, PATH_MAX, "%s/%s/%s", data_dir, sub_dir, CHARGE_NOW);
    file_current = fopen(buffer, "r");

    snprintf(buffer, PATH_MAX, "%s/%s/%s", data_dir, sub_dir, CHARGE_FULL);
    file_full = fopen(buffer, "r");

    if (file_current == NULL || file_full == NULL) {
        result = ERROR_FILE_NOT_FOUND;
        goto clean;
    }

    if (fscanf(file_current, "%ld", &current) != 1 ||
        fscanf(file_full, "%ld", &full) != 1) {
        result = ERROR_FILE_READING;
        goto clean;
    }

    *charge_level = (current / (float)full);

clean:
    if (file_current != NULL) fclose(file_current);
    if (file_full != NULL) fclose(file_full);

    return result;
}

static int read_charge_status(const char* data_dir,
                              const char* sub_dir,
                              char* buffer,
                              enum battery_powerstate* charge_status) {
    FILE* file = NULL;

    int result = 0;

    snprintf(buffer, PATH_MAX, "%s/%s/%s", data_dir, sub_dir, CHARGE_STATUS);
    file = fopen(buffer, "r");

    if (file == NULL) {
        result = ERROR_FILE_NOT_FOUND;
        goto clean;
    }

    if (fscanf(file, "%s", buffer) != 1) {
        result = ERROR_FILE_READING;
        goto clean;
    }

    if (strcmp(buffer, CHARGE_STATUS_CHARGING) == 0) {
        *charge_status = POWER_CHARGING;
    } else if (strcmp(buffer, CHARGE_STATUS_DISCHARGING) == 0) {
        *charge_status = POWER_DISCHARGING;
    } else if (strcmp(buffer, CHARGE_STATUS_FULL) == 0) {
        *charge_status = POWER_CHARGED;
    } else {
        result = ERROR_CHARGE_STATUS_UNKNOWN;
        goto clean;
    }

clean:
    if (file != NULL) fclose(file);

    return result;
}

int battery_get_state(struct battery_state* battery_state) {
    DIR* directory;
    struct dirent* dirent;
    char buffer[PATH_MAX];
    int result = 0;

    regex_t regex;

    bool data_found = false;

    if ((directory = opendir(DATADIR)) == NULL) {
        return ERROR_FOLDER_NOT_FOUND;
    }

    while ((dirent = readdir(directory)) != NULL) {
        snprintf(buffer, PATH_MAX, "%s/%s", DATADIR, dirent->d_name);

        if (regcomp(&regex, "BAT[0-9]+", REG_EXTENDED) != 0) {
            result = ERROR_REGEX_COMPILE;
            goto clean;
        }

        if (regexec(&regex, dirent->d_name, 0, NULL, 0) != 0) {
            regfree(&regex);
            continue;
        }

        regfree(&regex);

        result = read_charge_level(DATADIR, dirent->d_name,
                                   buffer, &battery_state->chargelevel);
        if (result != 0) {
            continue;
        }
        result = read_charge_status(DATADIR, dirent->d_name,
                                    buffer, &battery_state->powerstate);
        if (result == 0) {
            data_found = true;
            break;
        }
    }

    if (!data_found) {
        result = ERROR_REGEX_FIND;
        goto clean;
    }

clean:
    if (directory != NULL) closedir(directory);

    return result;
}

#endif