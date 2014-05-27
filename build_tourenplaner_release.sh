#!/bin/bash

# stop on any error
set -e

# find project directory
self=$(readlink -f "$0")
START=$(dirname "${self}")

cd "${START}"

(cd ToureNPlaner; gradle assembleRelease)

mv ToureNPlaner/build/apk/ToureNPlaner-release-unsigned.apk ./
