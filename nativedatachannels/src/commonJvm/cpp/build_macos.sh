#!/bin/sh
mkdir -p build-macos
pushd "build-macos" || exit
cmake -DCMAKE_OSX_ARCHITECTURES="x86_64 -arch arm64" ..
make
popd || exit