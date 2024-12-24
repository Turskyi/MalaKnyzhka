package com.turskyi.malaknyzhka

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

fun createSettings(): Settings {
    val preferences = Preferences.userRoot().node("MalaKnyzhkaSettings")
    return PreferencesSettings(preferences)
}