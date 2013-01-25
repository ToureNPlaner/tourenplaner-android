#!/bin/bash

START=$(pwd)

# create local.properties for the ToureNPlaner main application
cd ${START}/ToureNPlaner
android update project -p .

# create local.properties for the Actionbar library
cd ${START}/JakeWharton-ActionBarSherlock/library/
android update project -p .

cd ${START}
cp -a actionbar_build.xml JakeWharton-ActionBarSherlock/library/build.xml

cd ToureNPlaner
ant release

mv ${START}/ToureNPlaner/bin/ToureNPlaner-release-unsigned.apk ${START}
