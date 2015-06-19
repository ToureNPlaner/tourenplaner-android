#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <foo.keystore> <alias_name> <bar.apk>"
    echo "A keystore can be created with keytool -genkey -v -keystore foo.keystore -alias alias_name -keyalg RSA -validity 10000"
    echo "Existing aliases can be checked with keytool -list -keystore foo.keystore"
    exit 1
fi

#http://stackoverflow.com/a/7365898
jarsigner -digestalg SHA1 -sigalg MD5withRSA -verbose -keystore "$1" "$3" "$2"

echo "Checking if APK is verified..."
jarsigner -verify "$3"

