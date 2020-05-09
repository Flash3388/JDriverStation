#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <zconf.h>

#include "cpu.h"

#include "errors.h"


static int read_cpu_usage(char* buffer, const int buffer_size, float* usage) {
    const char* delim = " ";

    FILE* file;
    int total = 0;
    int idle = 0;
    int current_value;
    int index = 0;

    file = fopen("/proc/stat", "r");
    if (file == NULL) {
        return ERROR_FILE_NOT_FOUND;
    }

    if (fgets(buffer, buffer_size, file) == NULL) {
        fclose(file);
        return ERROR_FILE_READING;
    }

    fclose(file);

    strtok(buffer, delim);
    char* ptr = strtok(NULL, delim);
    while (ptr != NULL) {
        ++index;

        current_value = strtol(ptr, NULL, 10);
        total += current_value;
        if (index == 4) {
            idle = current_value;
        }

        ptr = strtok(NULL, delim);
    }

    *usage = 1.0 - (idle / (float) total);

    return 0;
}

int cpu_get_usage(float* usage) {
    char buffer[100];
    return read_cpu_usage(buffer, 100, usage);
}