package com.turskyi.malaknyzhka.usecases

import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.Experience

data class HostnameOverrides(
    val experience: Experience? = null,
    val language: AppLang? = null,
)

object HostnameSettingsResolver {
    fun resolve(hostname: String?): HostnameOverrides {
        if (hostname == null) return HostnameOverrides()

        val normalizedHostname = hostname.lowercase().removeSuffix(".")

        return when (normalizedHostname) {
            "en.malaknyzhka.shevchenkoai.com" -> HostnameOverrides(
                experience = Experience.BOOK,
                language = AppLang.English
            )
            "en.shevchenkoai.com" -> HostnameOverrides(
                language = AppLang.English
            )
            "en.taras.shevchenkoai.com" -> HostnameOverrides(
                experience = Experience.TARAS,
                language = AppLang.English
            )
            "malaknyzhka.shevchenkoai.com" -> HostnameOverrides(
                experience = Experience.BOOK
            )
            "taras.shevchenkoai.com" -> HostnameOverrides(
                experience = Experience.TARAS
            )
            "uk.malaknyzhka.shevchenkoai.com" -> HostnameOverrides(
                experience = Experience.BOOK,
                language = AppLang.Ukraine
            )
            "uk.shevchenkoai.com" -> HostnameOverrides(
                language = AppLang.Ukraine
            )
            "uk.taras.shevchenkoai.com" -> HostnameOverrides(
                experience = Experience.TARAS,
                language = AppLang.Ukraine
            )
            else -> HostnameOverrides()
        }
    }
}
