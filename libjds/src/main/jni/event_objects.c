
#include "event_objects.h"
#include "jni_objects.h"

#define PACKAGE(x) "com/flash3388/frc/ds/api/events/" #x


jobject make_joystick_count_event(JNIEnv* env, DS_JoystickEvent event) {
    jclass class = get_class(env, PACKAGE(JoystickCountChangeEvent));
    if (NULL == class) {
        return NULL;
    }
    jmethodID constructor = get_constructor(env, class, "(I)V");
    if (NULL == constructor) {
        return NULL;
    }

    return (*env)->NewObject(env, class, constructor, event.count);
}

jobject make_status_change_event(JNIEnv* env) {
    jclass class = get_class(env, PACKAGE(StatusStringChangeEvent));
    if (NULL == class) {
        return NULL;
    }
    jmethodID constructor = get_constructor(env, class, "(Ljava/lang/String;)V");
    if (NULL == constructor) {
        return NULL;
    }

    char* status = DS_GetStatusString();
    jstring str = (*env)->NewStringUTF(env, status);

    return (*env)->NewObject(env, class, constructor, str);
}

jobject get_java_control_mode(JNIEnv* env, DS_ControlMode control_mode) {
    jclass class = get_class(env, "com/flash3388/frc/ds/api/DsControlMode");
    if (NULL == class) {
        return NULL;
    }

    jfieldID field;
    switch (control_mode) {
        case DS_CONTROL_TEST:
            field = get_static_field(env, class, "TEST", "Lcom/flash3388/frc/ds/api/DsControlMode;");
            break;
        case DS_CONTROL_AUTONOMOUS:
            field = get_static_field(env, class, "AUTONOMOUS", "Lcom/flash3388/frc/ds/api/DsControlMode;");
            break;
        case DS_CONTROL_TELEOPERATED:
            field = get_static_field(env, class, "TELEOPERATED", "Lcom/flash3388/frc/ds/api/DsControlMode;");
            break;
    }

    return get_static_object_value(env, class, field);
}

jobject make_robot_state_event(JNIEnv* env, DS_RobotEvent event) {
    jclass class = get_class(env, PACKAGE(RobotStateChangeEvent));
    if (NULL == class) {
        return NULL;
    }
    jmethodID constructor = get_constructor(env, class, "(Lcom/flash3388/frc/ds/api/DsControlMode;ZZZDZDDDD)V");
    if (NULL == constructor) {
        return NULL;
    }

    return (*env)->NewObject(env, class, constructor,
                             get_java_control_mode(env, event.mode),
                             event.connected, event.code, event.enabled,
                             event.voltage, event.estopped,
                             event.cpu_usage, event.disk_usage, event.ram_usage,
                             event.can_util);
}

jobject make_radio_change_event(JNIEnv* env, DS_RadioEvent event) {
    jclass class = get_class(env, PACKAGE(RadioConnectionChangeEvent));
    if (NULL == class) {
        return NULL;
    }
    jmethodID constructor = get_constructor(env, class, "(Z)V");
    if (NULL == constructor) {
        return NULL;
    }

    return (*env)->NewObject(env, class, constructor, event.connected);
}

jobject make_fms_change_event(JNIEnv* env, DS_FMSEvent event) {
    jclass class = get_class(env, PACKAGE(FmsConnectionChangeEvent));
    if (NULL == class) {
        return NULL;
    }
    jmethodID constructor = get_constructor(env, class, "(Z)V");
    if (NULL == constructor) {
        return NULL;
    }

    return (*env)->NewObject(env, class, constructor, event.connected);
}

jobject make_netconsole_event(JNIEnv* env, DS_NetConsoleEvent event) {
    jclass class = get_class(env, PACKAGE(NetConsoleMessageEvent));
    if (NULL == class) {
        return NULL;
    }
    jmethodID constructor = get_constructor(env, class, "(Ljava/lang/String;)V");
    if (NULL == constructor) {
        return NULL;
    }

    jstring str = (*env)->NewStringUTF(env, event.message);

    return (*env)->NewObject(env, class, constructor, str);
}

jobject make_event_object(JNIEnv* env, DS_Event event) {
    switch (event.type) {
        case DS_JOYSTICK_COUNT_CHANGED:
            return make_joystick_count_event(env, event.joystick);
        case DS_STATUS_STRING_CHANGED:
            return make_status_change_event(env);
        case DS_ROBOT_MODE_CHANGED:
        case DS_ROBOT_COMMS_CHANGED:
        case DS_ROBOT_CODE_CHANGED:
        case DS_ROBOT_ENABLED_CHANGED:
        case DS_ROBOT_VOLTAGE_CHANGED:
        case DS_ROBOT_ESTOP_CHANGED:
        case DS_ROBOT_CPU_INFO_CHANGED:
        case DS_ROBOT_DISK_INFO_CHANGED:
        case DS_ROBOT_RAM_INFO_CHANGED:
        case DS_ROBOT_CAN_UTIL_CHANGED:
            return make_robot_state_event(env, event.robot);
        case DS_RADIO_COMMS_CHANGED:
            return make_radio_change_event(env, event.radio);
        case DS_FMS_COMMS_CHANGED:
            return make_fms_change_event(env, event.fms);
        case DS_NETCONSOLE_NEW_MESSAGE:
            return make_netconsole_event(env, event.netconsole);
        default:
            return NULL;
    }
}

/*
 *    DS_NULL_EVENT = 0x00,
   DS_FMS_COMMS_CHANGED = 0x01,
   DS_RADIO_COMMS_CHANGED = 0x03,
   DS_JOYSTICK_COUNT_CHANGED = 0x05,
   DS_NETCONSOLE_NEW_MESSAGE = 0x06,
   DS_ROBOT_ENABLED_CHANGED = 0x07,
   DS_ROBOT_MODE_CHANGED = 0x09,
   DS_ROBOT_REBOOTED = 0x0a,
   DS_ROBOT_COMMS_CHANGED = 0x0b,
   DS_ROBOT_CODE_CHANGED = 0x0d,
   DS_ROBOT_CODE_RESTARTED = 0x10,
   DS_ROBOT_VOLTAGE_CHANGED = 0x11,
   DS_ROBOT_CAN_UTIL_CHANGED = 0x12,
   DS_ROBOT_CPU_INFO_CHANGED = 0x13,
   DS_ROBOT_RAM_INFO_CHANGED = 0x14,
   DS_ROBOT_DISK_INFO_CHANGED = 0x15,
   DS_ROBOT_STATION_CHANGED = 0x16,
   DS_ROBOT_ESTOP_CHANGED = 0x17,
   DS_STATUS_STRING_CHANGED = 0x18,
 */
