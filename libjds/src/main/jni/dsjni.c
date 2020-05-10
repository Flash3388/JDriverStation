#include <stdlib.h>
#include <jni.h>
#include <stdio.h>

#include <LibDS.h>


JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_isInitialized
        (JNIEnv *env, jclass obj) {

    return DS_Initialized();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_initialize
        (JNIEnv *env, jclass obj) {

    DS_Init();
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_close
        (JNIEnv *env, jclass obj) {

    DS_Close();
    return 0;
}

