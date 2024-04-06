cmake_minimum_required(VERSION 3.22.1)

project("nativedatachannels")

set(CMAKE_CXX_STANDARD 11)

add_library(${CMAKE_PROJECT_NAME} SHARED
        native-lib.cpp)

if(ANDROID)
link_directories(${CMAKE_SOURCE_DIR}/../../../artifacts/android-native/${ANDROID_ABI})
target_link_directories(${CMAKE_PROJECT_NAME} PUBLIC ${CMAKE_SOURCE_DIR}/../../../artifacts/android-native/${ANDROID_ABI})
target_link_libraries(${CMAKE_PROJECT_NAME}
    android
    log
    datachannel)
endif()

if(NOT ANDROID)
    find_package(LibDataChannel REQUIRED)
    include_directories(${CMAKE_SOURCE_DIR})
    target_link_libraries(${CMAKE_PROJECT_NAME} LibDataChannel::LibDataChannelStatic)
endif()