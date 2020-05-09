///////////////////////////////////////////////////////////////////////
// Christopher Mitchell
// Linux Battery Level Checker
// May 13th, 2009
// http://www.cemetech.net
//
// This code isn't terribly groundbreaking, but please be respectful as
// far as reuse and modifications; credit me, or at least drop me an
// email to let me know you found my code helpful


// MODIFIED FROM https://www.cemetech.net/forum/viewtopic.php?t=3638&start=0
///////////////////////////////////////////////////////////////////////

#include "battery.h"

#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <dirent.h>
#include <linux/limits.h>
#include <regex.h>
#include <stdbool.h>

#include "errors.h"

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

/*#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <math.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>


#define PATH_BATT_STATE "/proc/acpi/battery/BAT0/state"
#define PATH_BATT_INFO "/proc/acpi/battery/BAT0/info"
#define BATT_READ_BUFLEN 256

int battery_get_state(struct battery_state* mybattstate) {
    printf("battery_get_state \n");
    int battStateHandle;
    long int battRate_mA=0;
    long int battMax_mAh=0;
    long int battRemain_mAh=0;
    char buffer[BATT_READ_BUFLEN];
    char tok_unit[8];
    int readoffset;
    short int readstate=0,readlen=0;      //0=reading,1=eol,2=eof

    if (-1 == (battStateHandle = open(PATH_BATT_INFO,O_RDONLY))) {
        return -1;
    }

    while (readstate < 2) {
        readoffset = 0;
        readstate = 0;
        while (!readstate) {
            if (0 > (readlen = read(battStateHandle,buffer+readoffset,1))) {
                //perror("Failed to read battery state");
                return -2;
            }
            if (!readlen) {
                readstate=2;
                break;
            }
            if ('\n' == *(buffer+readoffset)) {
                readstate++;
                *(buffer+readoffset+1) = '\0';
            }
            readoffset++;
        }
        if (readstate == 2) break;
        if (NULL != strstr(buffer,"last full capacity")) {
            if (0 >= sscanf(buffer+25,"%ld %s",&battMax_mAh,tok_unit)) {
                return -3;
            }
            break;
        }
    }
    close(battStateHandle);

    if (-1 == (battStateHandle = open(PATH_BATT_STATE,O_RDONLY))) {
        return -4;
    }

    readstate = 0;
    while (readstate < 2) {
        readoffset = 0;
        readstate = 0;
        while (!readstate) {
            if (0 > (readlen = read(battStateHandle,buffer+readoffset,1))) {
                //perror("Failed to read battery state");
                return -5;
            }
            if (0 == readlen) {
                readstate=2;
                break;
            }
            if ('\n' == *(buffer+readoffset)) {
                readstate++;
                *(buffer+readoffset+1) = '\0';
            }
            readoffset++;
        }
        if (readstate == 2) break;
        if (NULL != strstr(buffer,"charging state")) {
            if (NULL != strstr(buffer,"discharging")) mybattstate->powerstate = POWER_DISCHARGING;
            else if (NULL != strstr(buffer,"charged")) mybattstate->powerstate = POWER_CHARGED;
            else mybattstate->powerstate = POWER_CHARGING;
        } else if (NULL != strstr(buffer,"present rate")) {
            if (0 >= sscanf(buffer+25,"%ld %s",&battRate_mA,tok_unit)) {
                //perror("sscanf for battery rate failed");
                return -6;
            }
        } else if (NULL != strstr(buffer,"remaining capacity")) {
            if (0 >= sscanf(buffer+25,"%ld %s",&battRemain_mAh,tok_unit)) {
                //perror("sscanf for battery capacity");
                return -7;
            }
        }
    }
    close(battStateHandle);

    mybattstate->chargelevel = 100.00*((float)battRemain_mAh/(float)battMax_mAh);
    if (battRate_mA) {
        mybattstate->time_hour = floor(battRemain_mAh/battRate_mA);
        mybattstate->time_min = floor(60*(((float)battRemain_mAh/(float)battRate_mA)-mybattstate->time_hour));
    }

    printf("battery_get_state return\n");
    return 0;
}*/
