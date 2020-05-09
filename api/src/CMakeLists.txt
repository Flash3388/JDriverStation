cmake_minimum_required(VERSION 3.5.1)

FIND_PACKAGE(JNI REQUIRED)

include_directories(${JNI_INCLUDE_DIRS})
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/../libs/include)

add_library(ds
        SHARED
        main/jni/dsjni.c
)
