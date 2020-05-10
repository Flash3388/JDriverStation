#include <stdlib.h>
#include <jni.h>
#include <stdio.h>

#include <LibDS.h>


JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getJoystickCount
        (JNIEnv *env, jclass obj) {

    return DS_GetJoystickCount();
}

