package com.turskyi.malaknyzhka

import com.russhwolf.settings.Settings

// A helper class for settings interactions.
class PageSettings(private val settings: Settings) {
    private val firstPage = 0

    fun getCurrentPage(): Int =
        settings.getInt(SettingsKeys.CURRENT_PAGE, firstPage)

    fun saveCurrentPage(page: Int) {
        settings.putInt(SettingsKeys.CURRENT_PAGE, page)
    }
}