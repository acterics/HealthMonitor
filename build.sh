#/bin/bash

GRADLE="./gradlew"
$GRADLE assembleRelease
RELEASEFILE="${CIRCLE_ARTIFACTS}/healthmonitor-release-${CIRCLE_BUILD_NUM}-${CIRCLE_SHA1}.apk"
cp ./app/build/outputs/apk/app-release.apk ${RELEASEFILE}