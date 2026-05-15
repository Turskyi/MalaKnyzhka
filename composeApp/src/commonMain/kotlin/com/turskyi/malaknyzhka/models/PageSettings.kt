package com.turskyi.malaknyzhka.models

import com.russhwolf.settings.Settings

// A helper class for settings interactions.
class PageSettings(private val settings: Settings) {
    /**
     * Intentionally start at index 1 to skip the title/cover page and
     * provide content immediately on the first run.
     */
    private val firstPage = 1
    private val defaultLanguage = "uk"

    fun getCurrentPage(): Int = settings.getInt(
        SettingsKeys.CURRENT_PAGE,
        firstPage,
    )

    fun saveCurrentPage(page: Int) {
        settings.putInt(SettingsKeys.CURRENT_PAGE, page)
    }

    @Suppress("unused")
    fun getCurrentLanguage(): String {
        return settings.getString(
            SettingsKeys.CURRENT_LANGUAGE,
            defaultLanguage,
        )
    }

    fun saveCurrentLanguage(lang: String) {
        settings.putString(SettingsKeys.CURRENT_LANGUAGE, lang)
    }
}