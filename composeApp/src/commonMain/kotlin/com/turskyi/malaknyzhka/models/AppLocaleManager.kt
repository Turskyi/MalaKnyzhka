package com.turskyi.malaknyzhka.models

import androidx.compose.runtime.Composable

interface AppLocaleManager {
    // Returns current lang tag e.g. "en", "uk".
    fun getLocale(): String

    // Function to change the application's locale.
    fun setLocale(appLang: AppLang)
    fun hasUserEverSetLanguage(): Boolean
}

/**
 * Provides an instance of the platform-specific AppLocaleManager.
 * This manager is used to get the initial locale and to set new locales.
 */
@Composable
expect fun rememberAppLocaleManager(): AppLocaleManager