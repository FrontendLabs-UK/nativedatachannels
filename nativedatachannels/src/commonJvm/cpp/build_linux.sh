#!/bin/sh

zig c++ -target x86_64-linux-gnu -s -shared -I. -L/Users/mac/Downloads/multiplatform-library-template-main/nativedatachannels/src/jvmMain/resources -ldatachannel -o libnativedatachannels.so native-lib.cpp || exit

mv libnativedatachannels.so ../../jvmMain/resources