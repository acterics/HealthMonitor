#!/bin/bash
echo "Start copying to artifacts directory"
cd ..
GRADLE="./gradlew"
$GRADLE assembleRelease
$GRADLE crashlyticsUploadDistributionRelease
echo "Artifacts directory: ${CIRCLE_ARTIFACTS}"
RELEASEFILE="${CIRCLE_ARTIFACTS}/habrcicdcat-release-${CIRCLE_BUILD_NUM}-${CIRCLE_SHA1}.apk"
cp ./app/build/outputs/apk/app-release.apk ${RELEASEFILE}