import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleGmsGoogleServices)
    alias(libs.plugins.googleFirebaseCrashlytics)
}

val keyPropertiesFile: File = rootProject.file(
    "composeApp/src/androidMain/key.properties",
)

val keyProperties: Properties = Properties()

if (keyPropertiesFile.exists()) {
    keyProperties.load(keyPropertiesFile.inputStream())
} else {
    throw IllegalStateException("key.properties file is missing")
}

// Debug environment variables.
val signingKeyDebugPath: String = keyProperties.getProperty(
    "dev.SIGNING_KEY_DEBUG_PATH",
)

val signingKeyDebugPassword: String = keyProperties.getProperty(
    "dev.SIGNING_KEY_DEBUG_PASSWORD",
)

val signingKeyDebugKey: String = keyProperties.getProperty(
    "dev.SIGNING_KEY_DEBUG_KEY",
)

val signingKeyDebugKeyPassword: String = keyProperties.getProperty(
    "dev.SIGNING_KEY_DEBUG_KEY_PASSWORD",
)

// Release environment variables.
val signingKeyReleasePath: String = keyProperties.getProperty(
    "production.SIGNING_KEY_RELEASE_PATH",
)

val signingKeyReleasePassword: String = keyProperties.getProperty(
    "production.SIGNING_KEY_RELEASE_PASSWORD",
)

val signingKeyReleaseKey: String = keyProperties.getProperty(
    "production.SIGNING_KEY_RELEASE_KEY",
)

val signingKeyReleaseKeyPassword: String = keyProperties.getProperty(
    "production.SIGNING_KEY_RELEASE_KEY_PASSWORD",
)

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget: KotlinNativeTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer =
                    (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                        static = (static ?: mutableListOf()).apply {
                            // Serve sources to debug inside browser.
                            add(rootDirPath)
                            add(projectDirPath)
                        }
                    }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain: KotlinSourceSet by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.navigation.compose)
            api(libs.multiplatform.settings)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

        wasmJsMain.dependencies {

        }
    }
}

android {
    namespace = libs.versions.applicationId.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("dev") {
            if (System.getenv("FCI_BUILD_ID") != null) {
                storeFile = file(System.getenv("CM_KEYSTORE_PATH"))
                storePassword = System.getenv("CM_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("CM_KEY_ALIAS")
                keyPassword = System.getenv("CM_KEY_PASSWORD")
            } else {
                storeFile = file(signingKeyDebugPath)
                storePassword = signingKeyDebugPassword
                keyAlias = signingKeyDebugKey
                keyPassword = signingKeyDebugKeyPassword
            }
        }
        create("production") {
            if (System.getenv("FCI_BUILD_ID") != null) {
                storeFile = file(System.getenv("CM_KEYSTORE_PATH"))
                storePassword = System.getenv("CM_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("CM_KEY_ALIAS")
                keyPassword = System.getenv("CM_KEY_PASSWORD")
            } else {
                storeFile = file(signingKeyReleasePath)
                storePassword = signingKeyReleasePassword
                keyAlias = signingKeyReleaseKey
                keyPassword = signingKeyReleaseKeyPassword
            }
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("dev")
        }
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("production")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    androidResources {
        generateLocaleConfig = true
    }
}

dependencies {
    implementation(libs.androidx.ui.android)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.appcompat)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.turskyi.malaknyzhka.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = libs.versions.dockName.get()
            packageVersion = libs.versions.versionName.get()

            macOS {
                iconFile.set(
                    project.file(
                        "../composeApp/src/desktopMain/icons/icon.icns",
                    )
                )
                bundleID = libs.versions.applicationId.get()
                dockName = libs.versions.dockName.get()
            }
            windows {
                iconFile.set(
                    project.file(
                        "../composeApp/src/desktopMain/icons/icon.ico",
                    )
                )
            }
            linux {
                iconFile.set(
                    project.file(
                        "../composeApp/src/desktopMain/icons/icon.png",
                    )
                )
            }
        }
    }
}

repositories {
    mavenCentral()
    google()
}
