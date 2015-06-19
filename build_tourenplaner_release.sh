#!/bin/bash

# stop on any error
set -e

# find project directory
self=$(readlink -f "$0")
START=$(dirname "${self}")

cd "${START}"

(cd ToureNPlaner; ./gradlew assembleRelease)

mv ToureNPlaner/build/outputs/apk/ToureNPlaner-release-unsigned.apk ./

echo -e "\n\n\nNow sign the APK with ./sign.sh debug.keystore alias_name ToureNPlaner-release-unsigned.apk"
