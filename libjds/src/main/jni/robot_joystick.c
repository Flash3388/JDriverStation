#include <stdlib.h>
#include <jni.h>
#include <stdio.h>

#include <LibDS.h>


JNIEXPORT void JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_resetJoysticks
        (JNIEnv *env, jclass obj) {

    DS_JoysticksReset();
}

JNIEXPORT void JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_addJoystick
        (JNIEnv *env, jclass obj, jint axes, jint buttons, jint hats) {

    DS_JoysticksAdd(axes, buttons, hats);
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getJoystickCount
        (JNIEnv *env, jclass obj) {

    return DS_GetJoystickCount();
}

JNIEXPORT jfloat JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getJoystickAxis
        (JNIEnv *env, jclass obj, jint index, jint axis) {

    return DS_GetJoystickAxis(index, axis);
}

JNIEXPORT jboolean JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getJoystickButton
        (JNIEnv *env, jclass obj, jint index, jint button) {

    return DS_GetJoystickButton(index, button);
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getJoystickHat
        (JNIEnv *env, jclass obj, jint index, jint hat) {

    return DS_GetJoystickHat(index, hat);
}

JNIEXPORT void JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setJoystickAxis
        (JNIEnv *env, jclass obj, jint index, jint axis, jfloat value) {

    DS_SetJoystickAxis(index, axis, value);
}

JNIEXPORT void JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setJoystickButton
        (JNIEnv *env, jclass obj, jint index, jint button, jboolean value) {

    DS_SetJoystickButton(index, button, value);
}

JNIEXPORT void JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setJoystickHat
        (JNIEnv *env, jclass obj, jint index, jint hat, jint angle) {

    DS_SetJoystickHat(index, hat, angle);
}

