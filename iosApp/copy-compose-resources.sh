#!/bin/bash

# Copies Compose Multiplatform resources to the iOS app bundle.
# Compose's iOS resource loader expects:
#   <app>.app/compose-resources/composeResources/...

set -euo pipefail

COMPOSE_RESOURCES_SOURCE="${SRCROOT}/../composeApp/build/generated/compose/resourceGenerator/assembledResources"
TARGET_ARCH_MAIN=""

if [[ "$ARCHS" == *"arm64"* ]] && [[ "$PLATFORM_NAME" == iphonesimulator* ]]; then
    TARGET_ARCH_MAIN="iosSimulatorArm64Main"
elif [[ "$ARCHS" == *"arm64"* ]] && [[ "$PLATFORM_NAME" == iphoneos* ]]; then
    TARGET_ARCH_MAIN="iosArm64Main"
else
    echo "Unsupported architecture or platform: ARCHS=$ARCHS, PLATFORM_NAME=$PLATFORM_NAME"
    exit 1
fi

RESOURCES_PATH="${COMPOSE_RESOURCES_SOURCE}/${TARGET_ARCH_MAIN}/composeResources"

if [ ! -d "$RESOURCES_PATH" ]; then
    echo "Warning: Compose resources not found at $RESOURCES_PATH"
    exit 0
fi

RESOURCE_FOLDER_PATH="${UNLOCALIZED_RESOURCES_FOLDER_PATH:-${EXECUTABLE_FOLDER_PATH:-}}"
if [ -z "$RESOURCE_FOLDER_PATH" ]; then
    echo "Missing Xcode resource folder path"
    exit 1
fi

TARGET_ROOT="${BUILT_PRODUCTS_DIR}/${RESOURCE_FOLDER_PATH}/compose-resources"

echo "Copying Compose resources from: $RESOURCES_PATH"
rm -rf "$TARGET_ROOT"
mkdir -p "$TARGET_ROOT"
cp -R "$RESOURCES_PATH" "$TARGET_ROOT/"
echo "Compose resources copied to: $TARGET_ROOT/composeResources"
