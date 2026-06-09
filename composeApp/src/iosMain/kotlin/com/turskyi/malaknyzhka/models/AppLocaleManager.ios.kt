package com.turskyi.malaknyzhka.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages
import platform.Foundation.setValue

// Key for storing the user's preferred language CODE in NSUserDefaults.
private const val PREFERRED_IOS_LOCALE_CODE_KEY =
    "app_preferred_ios_locale_code"

// Key for storing whether the user has ever explicitly set the language.
private const val PREFERRED_IOS_LOCALE_USER_SET_KEY =
    "app_preferred_ios_locale_user_set"

class IosAppLocale : AppLocale {

    override fun getLocale(): String {
        // 1. Check if user has ever set a language within the app
        val userHasSetLanguage: Boolean =
            NSUserDefaults.standardUserDefaults.boolForKey(
                PREFERRED_IOS_LOCALE_USER_SET_KEY
            )

        if (userHasSetLanguage) {
            // 2. If yes, try to get their stored preference
            val storedLocale: String? =
                NSUserDefaults.standardUserDefaults.stringForKey(
                    PREFERRED_IOS_LOCALE_CODE_KEY
                )
            if (!storedLocale.isNullOrBlank()) {
                return storedLocale.split("-").firstOrNull()
                    ?: AppLang.DEFAULT.code
            }
        }

        // 3. If no in-app stored preference or user hasn't set one,
        //    get the app's current effective language.
        val appPreferredLocalization: String? =
            NSBundle.mainBundle.preferredLocalizations.firstOrNull() as? String
        if (appPreferredLocalization != null && appPreferredLocalization != "Base") {
            return appPreferredLocalization.split("-").firstOrNull()
                ?: AppLang.DEFAULT.code
        }

        val systemPreferredLanguage: String? =
            NSLocale.preferredLanguages.firstOrNull() as? String

        if (!systemPreferredLanguage.isNullOrBlank()) {
            return systemPreferredLanguage.split("-").firstOrNull()
                ?: AppLang.DEFAULT.code
        }

        return AppLang.DEFAULT.code
    }

    override fun setLocale(appLang: AppLang) {
        val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults

        // 1. Store the user's chosen language code.
        userDefaults.setValue(
            appLang.code,
            forKey = PREFERRED_IOS_LOCALE_CODE_KEY
        )

        // 2. Store the flag indicating the user has explicitly made a choice.
        userDefaults.setBool(
            true,
            forKey = PREFERRED_IOS_LOCALE_USER_SET_KEY
        )

        // 3. Set AppleLanguages to hint the system/bundles about the
        // preference.
        // This is necessary for some resource loading logic on iOS to respect
        // the change.
        userDefaults.setObject(
            listOf(appLang.code),
            forKey = "AppleLanguages",
        )

        // Ensure it's written to disk immediately.
        userDefaults.synchronize()
    }

    override fun hasUserEverSetLanguage(): Boolean {
        return NSUserDefaults.standardUserDefaults.boolForKey(
            PREFERRED_IOS_LOCALE_USER_SET_KEY,
        )
    }
}

@Composable
actual fun rememberAppLocale(): AppLocale {
    return remember { IosAppLocale() }
}
