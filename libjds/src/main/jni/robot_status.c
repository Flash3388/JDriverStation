#include <stdlib.h>
#include <jni.h>
#include <stdio.h>

#include <LibDS.h>


JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_hasRobotCode
        (JNIEnv *env, jclass obj) {

    return DS_GetRobotCode();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_isConnectedToFms
        (JNIEnv *env, jclass obj) {

    return DS_GetFMSCommunications();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_isConnectedToRobot
        (JNIEnv *env, jclass obj) {

    return DS_GetRobotCommunications();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_isConnectedToRadio
        (JNIEnv *env, jclass obj) {

    return DS_GetRadioCommunications();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getCpuUsage
        (JNIEnv *env, jclass obj) {

    return DS_GetRobotCPUUsage();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getRamUsage
        (JNIEnv *env, jclass obj) {

    return DS_GetRobotRAMUsage();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getDiskUsage
        (JNIEnv *env, jclass obj) {

    return DS_GetRobotDiskUsage();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getCanUsageUsage
        (JNIEnv *env, jclass obj) {

    return DS_GetRobotCANUtilization();
}

JNIEXPORT jfloat JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getRobotVoltage
        (JNIEnv *env, jclass obj) {

    return DS_GetRobotVoltage();
}

JNIEXPORT jfloat JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getMaximumRobotVoltage
        (JNIEnv *env, jclass obj) {

    return DS_GetMaximumBatteryVoltage();
}

JNIEXPORT jstring JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getStatusString
        (JNIEnv *env, jclass obj) {
    char* status_string = DS_GetStatusString();
    return (*env)->NewStringUTF(env, status_string);
}