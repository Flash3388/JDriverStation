#include <stdlib.h>
#include <jni.h>
#include <stdio.h>

#include <LibDS.h>


JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_rebootRobot
        (JNIEnv *env, jclass obj) {

    DS_RebootRobot();
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_restartRobotCode
        (JNIEnv *env, jclass obj) {

    DS_RestartRobotCode();
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setRobotEnabled
        (JNIEnv *env, jclass obj, jboolean enabled) {

    DS_SetRobotEnabled((int) enabled);
    return 0;
}


JNIEXPORT jboolean JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_isRobotEnabled
        (JNIEnv *env, jclass obj) {

    return DS_GetRobotEnabled();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setControlMode
        (JNIEnv *env, jclass obj, jint control_mode) {

    switch (control_mode) {
        case 0:
            DS_SetControlMode(DS_CONTROL_TELEOPERATED);
            break;
        case 1:
            DS_SetControlMode(DS_CONTROL_AUTONOMOUS);
            break;
        case 2:
            DS_SetControlMode(DS_CONTROL_TEST);
            break;
    }
    return 0;
}


JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getControlMode
        (JNIEnv *env, jclass obj) {
    DS_ControlMode control_mode = DS_GetControlMode();
    int result = -1;

    switch (control_mode) {
        case DS_CONTROL_TELEOPERATED:
            result = 0;
            break;
        case DS_CONTROL_AUTONOMOUS:
            result = 1;
            break;
        case DS_CONTROL_TEST:
            result = 2;
            break;
    }

    return result;
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setEmergencyStopped
        (JNIEnv *env, jclass obj, jboolean enabled) {

    DS_SetEmergencyStopped((int) enabled);
    return 0;
}


JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_isEmergencyStopped
        (JNIEnv *env, jclass obj) {

    return DS_GetEmergencyStopped();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_configureProtocol
        (JNIEnv *env, jclass obj, jint protocol) {

    DS_Protocol ds_protocol;

    switch (protocol) {
        case 0:
            ds_protocol = DS_GetProtocolFRC_2014();
            break;
        case 1:
            ds_protocol = DS_GetProtocolFRC_2015();
            break;
        case 2:
            ds_protocol = DS_GetProtocolFRC_2016();
            break;
        case 3:
            ds_protocol = DS_GetProtocolFRC_2020();
            break;
    }

    DS_ConfigureProtocol(&ds_protocol);
    return 0;
}
