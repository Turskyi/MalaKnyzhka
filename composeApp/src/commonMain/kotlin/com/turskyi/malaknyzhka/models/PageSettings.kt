package com.turskyi.malaknyzhka.models

import com.russhwolf.settings.Settings

// An interface for book settings interactions (Dependency Inversion Principle).
interface BookRepository {
    fun getCurrentPage(): Int
    fun saveCurrentPage(page: Int)
    fun getCurrentLanguage(): String
    fun saveCurrentLanguage(lang: String)
}

// A concrete implementation using Settings.
class SettingsBookRepository(private val settings: Settings) : BookRepository {
    /**
     * Intentionally start at index 1 to skip the title/cover page and
     * provide content immediately on the first run.
     */
    private val firstPage = 1
    private val defaultLanguage = "uk"

    override fun getCurrentPage(): Int = settings.getInt(
        SettingsKeys.CURRENT_PAGE,
        firstPage,
    )

    override fun saveCurrentPage(page: Int) {
        settings.putInt(SettingsKeys.CURRENT_PAGE, page)
    }

    override fun getCurrentLanguage(): String {
        return settings.getString(
            SettingsKeys.CURRENT_LANGUAGE,
            defaultLanguage,
        )
    }

    override fun saveCurrentLanguage(lang: String) {
        settings.putString(SettingsKeys.CURRENT_LANGUAGE, lang)
    }
}