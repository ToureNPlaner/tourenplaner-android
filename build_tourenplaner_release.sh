#!/bin/bash

# stop on any error
set -e

# find project directory
self=$(readlink -f "$0")
START=$(dirname "${self}")

cd "${START}"

# create local.properties for the ToureNPlaner main application
android update project -p ToureNPlaner

# Actionbar source isn't part of the project right now.
## create local.properties for the Actionbar library
# android update project -n ActionBarSherlock -p JakeWharton-ActionBarSherlock/library

### Shouldn't be needed with "-n ActionBarSherlock" above?
## cp -a actionbar_build.xml JakeWharton-ActionBarSherlock/library/build.xml

(cd ToureNPlaner; ant release)

mv ToureNPlaner/bin/ToureNPlaner-release-unsigned.apk ./
