#include <stdlib.h>
#include <jni.h>
#include <stdio.h>

#include <LibDS.h>

#include "event_objects.h"


JNIEXPORT jobject JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_pollEvent
        (JNIEnv *env, jclass obj) {
    DS_Event event;
    if (!DS_PollEvent(&event)) {
        return NULL;
    }

    return make_event_object(env, event);
}
