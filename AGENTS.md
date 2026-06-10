# AGENTS

Checklist for an AI agent onboarding to this repo

- [ ] Understand this is a Compose Multiplatform monolith (shared
  `:composeApp` module).
- [ ] Locate platform entrypoints: `composeApp/src/*Main` and `iosApp/iosApp`
  for iOS integration.
- [ ] Ensure build-time secret files exist (see `key.properties` /
  `google-services.json`) or CI patterns.
- [ ] Know the main development tasks for each target (wasm, Android, Desktop,
  iOS).

Quick high‑level summary

- Project type: Compose Multiplatform. One main module:
  `:composeApp` (shared UI + platform glue). iOS wrapper app lives in `iosApp/`
  and loads the Kotlin framework named `ComposeApp`.
- Shared code location: `composeApp/src/commonMain/kotlin` (UI, view-models,
  models). Platform-specific implementations follow the expect/actual pattern in
  `*Main` (e.g. `Platform.*.kt`, `createSettings.*.kt`).
- Important libs: `com.russhwolf:multiplatform-settings` for cross-platform
  settings, `androidx.navigation` + Compose for navigation.

Essential files and where to start

- Big picture: read `README.md` and `composeApp/build.gradle.kts` first.
- Module settings: `settings.gradle.kts` and `gradle/libs.versions.toml` (
  dependency versions and plugin aliases).
- CI snippets: `codemagic.yaml` (how the build injects `key.properties` and
  `google-services.json`) and `firebase.json` (wasm hosting path).
- iOS glue: `iosApp/iosApp/ContentView.swift` +
  `composeApp/src/iosMain/kotlin/com/turskyi/malaknyzhka/MainViewController.kt` (
  Compose UIViewController adapter).

Developer workflows (commands to run)

- Web (development):
    - ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
        - Dev server is configured to serve both the project root and module
          dir (use Chrome for best results, see `README.md`).
- Web (production artifact for hosting):
    - ./gradlew :composeApp:wasmJsBrowserProductionExecutableDistribution
        - The production build output expected by `firebase.json` is
          `composeApp/build/dist/wasmJs/productionExecutable`.
- Android (local debug / release):
    - ./gradlew :composeApp:assembleDebug
    - ./gradlew :composeApp:bundleRelease    (or `:composeApp:assembleRelease`)
- Desktop:
    - ./gradlew :composeApp:run (or use the Gradle run task for `desktop`
      target). The desktop main class is `com.turskyi.malaknyzhka.MainKt` (
      configured in `build.gradle.kts`).
- iOS (integration):
    - Build Kotlin framework via Gradle (e.g. build the iOS target/debug
      framework) and then open `iosApp/iosApp.xcodeproj` in Xcode. The iOS app
      calls `MainViewController()` from the generated `ComposeApp` framework.

Secrets & CI notes (critical - will break builds)

- `composeApp/build.gradle.kts` expects
  `composeApp/src/androidMain/key.properties` to exist; if missing Gradle will
  throw IllegalStateException. Keys the file should contain (examples):
    - `dev.SIGNING_KEY_DEBUG_PATH`, `dev.SIGNING_KEY_DEBUG_PASSWORD`,
      `dev.SIGNING_KEY_DEBUG_KEY`, `dev.SIGNING_KEY_DEBUG_KEY_PASSWORD`
    - `production.SIGNING_KEY_RELEASE_PATH`,
      `production.SIGNING_KEY_RELEASE_PASSWORD`,
      `production.SIGNING_KEY_RELEASE_KEY`,
      `production.SIGNING_KEY_RELEASE_KEY_PASSWORD`
- CI (Codemagic) decodes environment variables into
  `composeApp/src/androidMain/key.properties` and
  `composeApp/google-services.json` (see `codemagic.yaml`). Local tests must
  create the file or set equivalent envs.

Project-specific patterns & gotchas

- Monolith orientation: everything is inside `:composeApp` (UI + Android
  manifest + wasm config + desktop entry). Expect the module to be large and
  cross-target.
- expect/actual platform surface: `Platform.kt` defines
  `expect fun getPlatform()` with `actual` implementations in `androidMain`,
  `iosMain`, `desktopMain`, `wasmJsMain`. Use that to detect platform behaviour
  or initial route handling.
    - Example: `composeApp/src/wasmJsMain/kotlin/.../Platform.wasmJs.kt` reads
      `window.location.hash` to populate `initialRoute`.
- Settings API: The code uses Russhwolf multiplatform-settings with
  platform-specific factories:
    - Android: `createSettings(context: Context)` -> `SharedPreferencesSettings`
    - iOS: `createDataStore()` -> `NSUserDefaultsSettings` (exposed to Swift as
      `createDataStore()`)
    - WASM/Desktop: `createSettings()` -> `StorageSettings` /
      `StorageSettings()`
- Navigation on web: `main.kt` in `wasmJsMain` binds window navigation to the
  NavController with `window.bindToNavigation(navController)`; routing relies on
  URL hash.
- Web dev experience: Webpack dev server is configured to serve project root &
  module dir so source maps can be traced in the browser - check
  `composeApp/build.gradle.kts` lines that add `rootDir` and `projectDir` to
  `devServer.static`.

Where to make safe changes

- Keep shared UI and business logic in `composeApp/src/commonMain`. Small
  platform tweaks go into `*Main` source sets (e.g. `createSettings.*.kt`,
  `Platform.*.kt`).
- Resource accessors are generated under `composeApp/build/generated` - do not
  edit generated files.

Quick troubleshooting checklist for an agent making a code change

- Before pushing, run the target builds locally that your change affects (e.g.,
  wasm dev run for web UI changes; `:composeApp:assembleDebug` for Android).
- If CI fails with signing or missing files, check whether
  `composeApp/src/androidMain/key.properties` and
  `composeApp/google-services.json` are present. CI may expect base64-encoded
  environment variables (see `codemagic.yaml`).
- To debug wasm issues use Chrome DevTools (source-maps are available if dev
  server runs with the repo root in static paths).

Useful file map (quick reference)

- Root: `README.md`, `codemagic.yaml`, `firebase.json`,
  `gradle/libs.versions.toml`, `settings.gradle.kts`
- Shared module: `composeApp/` (main Gradle file `composeApp/build.gradle.kts`)
- Shared code: `composeApp/src/commonMain/kotlin` (UI, models, view-models)
- Platform glue: `composeApp/src/*Main/kotlin` (androidMain, iosMain,
  desktopMain, wasmJsMain)
- iOS wrapper: `iosApp/iosApp/*` (SwiftUI entry; loads Kotlin framework)

If you need more: run these file reads first

- `composeApp/build.gradle.kts` (already useful for build rules)
- `composeApp/src/commonMain/kotlin/com/turskyi/malaknyzhka/ui/App.kt` (app
  structure)
- `composeApp/src/wasmJsMain/kotlin/.../main.kt` and
  `composeApp/src/desktopMain/kotlin/.../main.kt` (entrypoints)

# This is an end of AGENTS.md and this line should never be below line number 200. If we need to add something to this file, we simply have to remove something less critical.

