#pragma once

#include <jni.h>
#include <LibDS.h>


jobject make_event_object(JNIEnv* env, DS_Event event);
