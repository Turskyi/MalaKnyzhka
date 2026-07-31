package com.turskyi.malaknyzhka.usecases

import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.Experience
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HostnameSettingsResolverTest {

    @Test
    fun testResolveEnMalaknyzhka() {
        val result = HostnameSettingsResolver.resolve("en.malaknyzhka.shevchenkoai.com")
        assertEquals(Experience.BOOK, result.experience)
        assertEquals(AppLang.English, result.language)
    }

    @Test
    fun testResolveEn() {
        val result = HostnameSettingsResolver.resolve("en.shevchenkoai.com")
        assertNull(result.experience)
        assertEquals(AppLang.English, result.language)
    }

    @Test
    fun testResolveEnTaras() {
        val result = HostnameSettingsResolver.resolve("en.taras.shevchenkoai.com")
        assertEquals(Experience.TARAS, result.experience)
        assertEquals(AppLang.English, result.language)
    }

    @Test
    fun testResolveMalaknyzhka() {
        val result = HostnameSettingsResolver.resolve("malaknyzhka.shevchenkoai.com")
        assertEquals(Experience.BOOK, result.experience)
        assertNull(result.language)
    }

    @Test
    fun testResolveTaras() {
        val result = HostnameSettingsResolver.resolve("taras.shevchenkoai.com")
        assertEquals(Experience.TARAS, result.experience)
        assertNull(result.language)
    }

    @Test
    fun testResolveUkMalaknyzhka() {
        val result = HostnameSettingsResolver.resolve("uk.malaknyzhka.shevchenkoai.com")
        assertEquals(Experience.BOOK, result.experience)
        assertEquals(AppLang.Ukraine, result.language)
    }

    @Test
    fun testResolveUk() {
        val result = HostnameSettingsResolver.resolve("uk.shevchenkoai.com")
        assertNull(result.experience)
        assertEquals(AppLang.Ukraine, result.language)
    }

    @Test
    fun testResolveUkTaras() {
        val result = HostnameSettingsResolver.resolve("uk.taras.shevchenkoai.com")
        assertEquals(Experience.TARAS, result.experience)
        assertEquals(AppLang.Ukraine, result.language)
    }

    @Test
    fun testResolveUnknown() {
        val result = HostnameSettingsResolver.resolve("unknown.com")
        assertNull(result.experience)
        assertNull(result.language)
    }

    @Test
    fun testResolveNull() {
        val result = HostnameSettingsResolver.resolve(null)
        assertNull(result.experience)
        assertNull(result.language)
    }

    @Test
    fun testResolveCaseInsensitiveAndTrailingDot() {
        val result = HostnameSettingsResolver.resolve("EN.Taras.ShevchenkoAI.com.")
        assertEquals(Experience.TARAS, result.experience)
        assertEquals(AppLang.English, result.language)
    }
}
