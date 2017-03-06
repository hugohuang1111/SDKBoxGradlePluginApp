#!/usr/bin/env bash

APP_BUILD_GRADLE="./app/build.gradle"

# disable sdkbox gradle plugin
linecount=`cat $APP_BUILD_GRADLE | wc -l`
sdkbox_line=20
begin_linecount=`expr $linecount - $sdkbox_line`
sed -E -i '.bak' "$begin_linecount, $linecount s/^(\/)*\* SDKBox BEGIN/\/\* SDKBox BEGIN/" "$APP_BUILD_GRADLE"
sed -E -i '.bak' "$begin_linecount, $linecount s/^(\/)* SDKBox End \*\// SDKBox End \*\//" "$APP_BUILD_GRADLE"

# upload sdkbox gradle plugin
if ! ./gradlew uploadArchives; then
    echo "Upload Archives Failed"
    exit 1
fi

#clean project
if ! ./gradlew clean; then
    echo "Clean Failed"
    exit 1
fi

# enable sdkbox gradle plugin
sed -E -i '.bak' "$begin_linecount,$linecount s/(\/)*\* SDKBox BEGIN/\/\/\/\* SDKBox BEGIN/" "$APP_BUILD_GRADLE"
sed -E -i '.bak' "$begin_linecount,$linecount s/(\/)* SDKBox End \*\//\/\/ SDKBox End \*\//" "$APP_BUILD_GRADLE"

if ! ./gradlew assembleDebug; then
    echo "assemble debug Failed"
    exit 1
fi

echo 'Done'
