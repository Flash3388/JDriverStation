cmake_minimum_required(VERSION 3.5.1)

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_CURRENT_SOURCE_DIR}/../../cmake")

FIND_PACKAGE(JNI REQUIRED)

add_library(jds
        SHARED
        main/jni/dsjni.c
        main/jni/robot_control.c
        main/jni/robot_status.c
        main/jni/robot_comm.c
        main/jni/robot_team.c
        main/jni/robot_joystick.c)

find_package(libds REQUIRED)

target_link_libraries(jds ${LIBDS_LIBRARY})
include_directories(
        ${LIBDS_INCLUDE_DIR}
        ${JNI_INCLUDE_DIRS}
)