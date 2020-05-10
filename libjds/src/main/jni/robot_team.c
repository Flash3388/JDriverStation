#include <stdlib.h>
#include <jni.h>
#include <stdio.h>

#include <LibDS.h>


JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setTeamNumber
        (JNIEnv *env, jclass obj, jint team_number) {

    DS_SetTeamNumber((int) team_number);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getTeamNumber
        (JNIEnv *env, jclass obj) {

    return DS_GetTeamNumber();
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setTeamAlliance
        (JNIEnv *env, jclass obj, jint team_alliance) {

    DS_Alliance alliance = DS_ALLIANCE_BLUE;
    switch (team_alliance) {
        case 0:
            alliance = DS_ALLIANCE_BLUE;
            break;
        case 1:
            alliance = DS_ALLIANCE_RED;
            break;
    }

    DS_SetAlliance(alliance);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getTeamAlliance
        (JNIEnv *env, jclass obj) {

    DS_Alliance alliance = DS_GetAlliance();
    int result = 0;

    switch (alliance) {
        case DS_ALLIANCE_BLUE:
            result = 0;
            break;
        case DS_ALLIANCE_RED:
            result = 1;
            break;
    }

    return (jint) result;
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_setTeamPosition
        (JNIEnv *env, jclass obj, jint team_position) {

    DS_Position position = DS_POSITION_1;
    switch (team_position) {
        case 0:
            position = DS_POSITION_1;
            break;
        case 1:
            position = DS_POSITION_2;
            break;
        case 2:
            position = DS_POSITION_3;
            break;
    }

    DS_SetPosition(position);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_flash3388_frc_ds_api_DriverStationJNI_getTeamPosition
        (JNIEnv *env, jclass obj) {

    DS_Position position = DS_GetPosition();
    int result = 0;

    switch (position) {
        case DS_POSITION_1:
            result = 0;
            break;
        case DS_POSITION_2:
            result = 1;
            break;
        case DS_POSITION_3:
            result = 2;
            break;
    }

    return (jint) result;
}
