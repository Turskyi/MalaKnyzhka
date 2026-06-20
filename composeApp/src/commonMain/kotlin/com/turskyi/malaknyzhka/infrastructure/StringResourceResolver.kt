package com.turskyi.malaknyzhka.infrastructure

import com.turskyi.malaknyzhka.models.AppLang
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.getSystemResourceEnvironment

/**
 * Utility to resolve string resources in a specific language, regardless of current app locale.
 */
@OptIn(InternalResourceApi::class)
object StringResourceResolver {
    /**
     * Retrieves a string resource for the Ukrainian language.
     * Uses @Suppress to access internal ResourceEnvironment constructor.
     */
    @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
    suspend fun getStringInUkrainian(resource: StringResource): String {
        val currentEnv = getSystemResourceEnvironment()
        val ukrainianEnv = ResourceEnvironment(
            language = LanguageQualifier(AppLang.Ukraine.code),
            region = currentEnv.region,
            theme = currentEnv.theme,
            density = currentEnv.density
        )
        return getString(ukrainianEnv, resource)
    }
}
