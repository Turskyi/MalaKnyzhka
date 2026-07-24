package com.turskyi.malaknyzhka.models

import com.russhwolf.settings.Settings

interface UserSettingsRepository {
    fun getThemeMode(): ThemeMode
    fun saveThemeMode(mode: ThemeMode)
    fun isOnboardingComplete(): Boolean
    fun saveOnboardingComplete(isComplete: Boolean)
    fun getExperience(defaultExperience: Experience): Experience
    fun saveExperience(experience: Experience)
}

class SettingsUserSettingsRepository(private val settings: Settings) :
    UserSettingsRepository {
    override fun getThemeMode(): ThemeMode {
        val modeName: String = settings.getString(
            SettingsKeys.THEME_MODE,
            ThemeMode.SYSTEM.name,
        )
        return try {
            ThemeMode.valueOf(modeName)
        } catch (e: IllegalArgumentException) {
            println("Invalid theme mode: $modeName. Error: $e")
            ThemeMode.SYSTEM
        }
    }

    override fun saveThemeMode(mode: ThemeMode) {
        settings.putString(SettingsKeys.THEME_MODE, mode.name)
    }

    override fun isOnboardingComplete(): Boolean {
        return settings.getBoolean(SettingsKeys.IS_ONBOARDING_COMPLETE, false)
    }

    override fun saveOnboardingComplete(isComplete: Boolean) {
        settings.putBoolean(SettingsKeys.IS_ONBOARDING_COMPLETE, isComplete)
    }

    override fun getExperience(defaultExperience: Experience): Experience {
        val experienceName: String = settings.getString(
            SettingsKeys.EXPERIENCE,
            defaultExperience.name,
        )
        return try {
            Experience.valueOf(experienceName)
        } catch (e: IllegalArgumentException) {
            println("Invalid experience: $experienceName. Error: $e")
            defaultExperience
        }
    }

    override fun saveExperience(experience: Experience) {
        settings.putString(SettingsKeys.EXPERIENCE, experience.name)
    }
}
