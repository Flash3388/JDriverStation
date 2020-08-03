cmake_minimum_required(VERSION 3.5.1)
project(libcomp C)

set(CMAKE_C_STANDARD 11)

FIND_PACKAGE(JNI REQUIRED)

include_directories(${JNI_INCLUDE_DIRS})
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/../libs/include)

add_library(comp
        SHARED
        main/jni/compjni.c
        main/jni/cpu.c
        main/jni/battery.c)
