#!/bin/sh

zig c++ -target x86_64-windows-gnu -shared -I. -L/Users/mac/Downloads/multiplatform-library-template-main/nativedatachannels/src/jvmMain/resources -ldatachannel -o libnativedatachannels.dll native-lib.cpp || exit

mv libnativedatachannels.dll libnativedatachannels.pdb ../../jvmMain/resources