#include <stdlib.h>
#include <jni.h>
#include <stdio.h>

#include "battery.h"
#include "cpu.h"

JNIEXPORT jint JNICALL Java_com_flash3388_frc_ds_comp_ComputerStatusJNI_getBatteryState
        (JNIEnv *env, jclass obj, jfloatArray output_data) {

    struct battery_state state;
    int result = battery_get_state(&state);

    jfloat * body = (*env)->GetFloatArrayElements(env, output_data, 0);
    body[0] = state.chargelevel;
    body[1] = state.powerstate;
    (*env)->ReleaseFloatArrayElements(env, output_data, body, 0);

    return (jint) result;
}

JNIEXPORT jint JNICALL Java_com_flash3388_frc_ds_comp_ComputerStatusJNI_getCpuUsage
        (JNIEnv *env, jclass obj, jfloatArray output_data) {

    float usage;
    int result = cpu_get_usage(&usage);

    jfloat * body = (*env)->GetFloatArrayElements(env, output_data, 0);
    body[0] = usage;
    (*env)->ReleaseFloatArrayElements(env, output_data, body, 0);

    return (jint) result;
}