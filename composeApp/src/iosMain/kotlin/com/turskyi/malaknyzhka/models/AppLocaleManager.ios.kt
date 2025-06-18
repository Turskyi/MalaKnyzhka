package com.turskyi.malaknyzhka.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSBundle
import platform.Foundation.NSLocale
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages
import platform.Foundation.setValue
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

// Key for storing the user's preferred language CODE in NSUserDefaults.
private const val PREFERRED_IOS_LOCALE_CODE_KEY =
    "app_preferred_ios_locale_code"

// Key for storing whether the user has ever explicitly set the language.
private const val PREFERRED_IOS_LOCALE_USER_SET_KEY =
    "app_preferred_ios_locale_user_set"

class IosAppLocaleManager : AppLocaleManager {

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
                    ?: AppLang.Ukraine.code
            }
            // If flag is true but code is missing/blank (should be rare if setLocale is correct),
            // fall through to system/default.
        }

        // 3. If no in-app stored preference or user hasn't set one,
        //    get the app's current effective language based on its Info.plist localizations
        //    and the system's preferred languages. This is often more reliable than just NSLocale.currentLocale.
        val appPreferredLocalization: String? =
            NSBundle.mainBundle.preferredLocalizations.firstOrNull() as? String
        if (appPreferredLocalization != null && appPreferredLocalization != "Base") {
            return appPreferredLocalization.split("-").firstOrNull()
                ?: AppLang.Ukraine.code
        }

        // 4. Fallback: Get the system's preferred language (e.g. from device
        // settings) NSLocale.currentLocale.languageCode can sometimes be less
        // specific than preferredLanguages.
        val systemPreferredLanguage: String? =
            NSLocale.preferredLanguages.firstOrNull() as? String

        if (!systemPreferredLanguage.isNullOrBlank()) {
            return systemPreferredLanguage.split("-").firstOrNull()
                ?: AppLang.Ukraine.code
        }

        // 5. Absolute fallback to a hardcoded default (e.g., your primary
        // development language)
        return AppLang.Ukraine.code
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

        // Ensure it's written to disk immediately.
        userDefaults.synchronize()

        val settingsUrl = NSURL(string = UIApplicationOpenSettingsURLString)
        settingsUrl.let {
            if (UIApplication.sharedApplication.canOpenURL(it)) {
                UIApplication.sharedApplication.openURL(
                    it,
                    options = emptyMap<Any?, Any>(),
                    completionHandler = null,
                )
            } else {
                println("Cannot open app settings URL")
            }
        }
    }

    override fun hasUserEverSetLanguage(): Boolean {
        return NSUserDefaults.standardUserDefaults.boolForKey(
            PREFERRED_IOS_LOCALE_USER_SET_KEY,
        )
    }
}

@Composable
actual fun rememberAppLocaleManager(): AppLocaleManager {
    return remember { IosAppLocaleManager() }
}