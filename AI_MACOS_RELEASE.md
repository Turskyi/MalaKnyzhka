# macOS App Store Release Guide (AI Agent Focused)

This guide provides the technical nuances required for an AI agent to
successfully release the macOS version of this Compose Multiplatform app to the
Mac App Store.

## Prerequisites & Credentials

To execute the release, you need access to the following (ask the user if not
found):

1. **3rd Party Mac Developer Application Certificate**: For signing the `.app`
   bundle.
2. **3rd Party Mac Developer Installer Certificate**: For signing the `.pkg`
   package.
3. **Mac App Store Provisioning Profile**: For `com.turskyi.malaknyzhka`. Ensure
   it is placed at
   `composeApp/src/desktopMain/entitlements/app.provisionprofile`.
4. **Correct Entitlements**: The `com.apple.application-identifier` in
   `composeApp/src/desktopMain/entitlements/entitlements.plist` must match the
   Provisioning Profile.
5. **App-Specific Password**: For uploading via `altool`.
6. **Apple ID**: `dmytro.turskyi@gmail.com`.
7. **Provider ID**: `48e54131-a786-4846-a40d-b5dacfbf27b2`.

## Critical Nuances

### 1. Quarantine Attributes

Apple strictly rejects packages containing files with the `com.apple.quarantine`
extended attribute.
**Action**: Run `xattr -r -d com.apple.quarantine .` before packaging.

### 2. Mandatory Icon Size (REGENERATION REQUIRED)

The Mac App Store requires a 1024x1024 (512pt @2x) icon in the `.icns` file.
The Compose Gradle plugin often fails to include all sizes or correctly map them
for TestFlight.
**Action**: Regenerate `icon.icns` from `icon.png` using `sips` and
`iconutil` before building.

### 3. arm64 Support & Deployment Target

To support only `arm64` (Apple Silicon) without Intel (`x86_64`), the
`LSMinimumSystemVersion` in `Info.plist` **must** be set to `12.0` or higher.

### 4. Entitlements Alignment (CRITICAL)

The `com.apple.application-identifier` in the signed binary **must** match the
provisioning profile exactly for TestFlight eligibility.
**Manual Action**: Ensure `entitlements.plist` contains:

```xml

<key>com.apple.application-identifier</key><string>
YOUR_TEAM_ID.com.turskyi.malaknyzhka
</string>
```

(Current Team ID: `26QZ8BPZFL`)

### 5. Manual Provisioning Profile Embedding

Compose Multiplatform might not always embed the profile correctly for the App
Store's subcomponent check.
**Action**: Manually copy the profile to
`Мала Книжка (Тарас Шевченко).app/Contents/embedded.provisionprofile` and sign
it before signing the main bundle.

### 6. JVM Hardened Runtime (MANDATORY FOR LAUNCH)

**Warning**: If the app installs but **fails to open** (nothing happens when
clicking "Open" in TestFlight), the JVM is likely being blocked by the Sandbox.
Compose apps (JVM) **must** have these entitlements enabled in
`entitlements.plist`:

```xml

<key>com.apple.security.cs.allow-jit</key><true /><key>
com.apple.security.cs.allow-unsigned-executable-memory
</key><true /><key>com.apple.security.cs.disable-library-validation
</key><true /><key>com.apple.security.cs.allow-dyld-environment-variables
</key><true />
```

Without these, the OS will kill the app immediately upon launch because it
attempts to generate/execute code in memory.

## Step-by-Step Execution Command

```bash
# 0. Confirm the version you intend to ship
grep -n 'versionName' gradle/libs.versions.toml

# 1. Hard-clean build outputs
./gradlew :composeApp:clean
rm -rf composeApp/build/compose/binaries/main-release

# 2. Regenerate the .icns file
mkdir -p composeApp/src/desktopMain/icons/icon.iconset
sips -z 16 16     composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_16x16.png
sips -z 32 32     composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_16x16@2x.png
sips -z 32 32     composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_32x32.png
sips -z 64 64     composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_32x32@2x.png
sips -z 128 128   composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_128x128.png
sips -z 256 256   composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_128x128@2x.png
sips -z 256 256   composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_256x256.png
sips -z 512 512   composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_256x256@2x.png
sips -z 512 512   composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_512x512.png
sips -z 1024 1024 composeApp/src/desktopMain/icons/icon.png --out composeApp/src/desktopMain/icons/icon.iconset/icon_512x512@2x.png
iconutil -c icns composeApp/src/desktopMain/icons/icon.iconset
rm -rf composeApp/src/desktopMain/icons/icon.iconset

# 3. Clean quarantine
xattr -r -d com.apple.quarantine .

# 4. Build base bundle
./gradlew :composeApp:packageReleasePkg || true

# 5. Manual Fixes & Signing
APP_NAME="Мала Книжка (Тарас Шевченко)"
APP_BUNDLE="composeApp/build/compose/binaries/main-release/app/${APP_NAME}.app"
IDENTITY="3rd Party Mac Developer Application: DMYTRO TURSKYI (26QZ8BPZFL)"
IDENTITY_INSTALLER="3rd Party Mac Developer Installer: DMYTRO TURSKYI (26QZ8BPZFL)"
ENTITLEMENTS="composeApp/src/desktopMain/entitlements/entitlements.plist"
CHILD_ENTITLEMENTS="composeApp/src/desktopMain/entitlements/child-entitlements.plist"
PROVISIONING_PROFILE="composeApp/src/desktopMain/entitlements/app.provisionprofile"

# Remove problematic subcomponent
rm -f "${APP_BUNDLE}/Contents/app.provisionprofile"

# Align Info.plist
/usr/libexec/PlistBuddy -c "Set :LSMinimumSystemVersion 12.0" "${APP_BUNDLE}/Contents/Info.plist"
/usr/libexec/PlistBuddy -c "Delete :ITSAppUsesNonExemptEncryption" "${APP_BUNDLE}/Contents/Info.plist" || true
/usr/libexec/PlistBuddy -c "Add :ITSAppUsesNonExemptEncryption bool false" "${APP_BUNDLE}/Contents/Info.plist"
/usr/libexec/PlistBuddy -c "Set :LSApplicationCategoryType public.app-category.books" "${APP_BUNDLE}/Contents/Info.plist"

# Fix Icon Reference
/usr/libexec/PlistBuddy -c "Set :CFBundleIconFile icon.icns" "${APP_BUNDLE}/Contents/Info.plist" || /usr/libexec/PlistBuddy -c "Add :CFBundleIconFile string icon.icns" "${APP_BUNDLE}/Contents/Info.plist"

# Manual Embed Provisioning Profile
cp "${PROVISIONING_PROFILE}" "${APP_BUNDLE}/Contents/embedded.provisionprofile"

# Deep Signing Fix: Sign all `dylibs` manually
find "${APP_BUNDLE}/Contents/app" -type f \( -name "*.dylib" -o -name "*.so" -o -name "*.jnilib" \) -exec codesign -s "${IDENTITY}" -vvvv --timestamp --options runtime --force {} \;
find "${APP_BUNDLE}/Contents/runtime" -type f \( -name "*.dylib" -o -name "*.so" \) -exec codesign -s "${IDENTITY}" -vvvv --timestamp --options runtime --force {} \;

# Sign jspawnhelper with child entitlements
JSPAWNHELPER="${APP_BUNDLE}/Contents/runtime/Contents/Home/lib/jspawnhelper"
if [ -f "${JSPAWNHELPER}" ]; then
    codesign -s "${IDENTITY}" -vvvv --timestamp --options runtime --entitlements "${CHILD_ENTITLEMENTS}" --force "${JSPAWNHELPER}"
fi

# SIGN MAIN EXECUTABLE (CRITICAL: MUST INCLUDE ENTITLEMENTS TO WORK IN SANDBOX)
codesign -s "${IDENTITY}" -vvvv --timestamp --options runtime --entitlements "${ENTITLEMENTS}" --force "${APP_BUNDLE}/Contents/MacOS/${APP_NAME}"

# Sign the app bundle itself
codesign -s "${IDENTITY}" -vvvv --timestamp --options runtime --entitlements "${ENTITLEMENTS}" --force "${APP_BUNDLE}"

# Preflight Check
codesign --verify --deep --strict --verbose=4 "${APP_BUNDLE}"

# Re-package
mkdir -p "composeApp/build/compose/binaries/main-release/pkg"
OUTPKG="composeApp/build/compose/binaries/main-release/pkg/MalaKnyzhka-$(/usr/libexec/PlistBuddy -c 'Print :CFBundleShortVersionString' "${APP_BUNDLE}/Contents/Info.plist")-manual.pkg"
productbuild --component "${APP_BUNDLE}" /Applications --sign "${IDENTITY_INSTALLER}" "${OUTPKG}"

# 6. Upload
xcrun altool --upload-app -f "${OUTPKG}" -t macos -u "dmytro.turskyi@gmail.com" -p "bhse-sbex-dofp-aqfy" --provider-public-id "48e54131-a786-4846-a40d-b5dacfbf27b2"
```
