package com.turskyi.malaknyzhka.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turskyi.malaknyzhka.getPlatform
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.AppLocale
import com.turskyi.malaknyzhka.models.Experience
import com.turskyi.malaknyzhka.models.ThemeMode
import com.turskyi.malaknyzhka.models.UserSettingsRepository
import com.turskyi.malaknyzhka.usecases.HostnameSettingsResolver
import com.turskyi.malaknyzhka.usecases.isOnAndroid
import com.turskyi.malaknyzhka.usecases.isOnDesktop
import com.turskyi.malaknyzhka.usecases.isOnIos
import com.turskyi.malaknyzhka.usecases.isOnWeb
import com.turskyi.malaknyzhka.usecases.toApLang
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val appLocale: AppLocale,
    private val userSettingsRepository: UserSettingsRepository,
) : ViewModel() {
    private val overrides = HostnameSettingsResolver.resolve(getPlatform().hostname)

    private val _appGlobalLanguage: MutableStateFlow<AppLang> =
        MutableStateFlow(overrides.language ?: appLocale.getLocale().toApLang())
    val appGlobalLanguage: StateFlow<AppLang> = _appGlobalLanguage.asStateFlow()

    private val _themeMode: MutableStateFlow<ThemeMode> =
        MutableStateFlow(userSettingsRepository.getThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val defaultExperience: Experience = if (isOnIos() || isOnDesktop() || isOnWeb()) {
        Experience.TARAS
    } else {
        Experience.BOOK
    }

    private val _experience: MutableStateFlow<Experience> =
        MutableStateFlow(overrides.experience ?: userSettingsRepository.getExperience(defaultExperience))
    val experience: StateFlow<Experience> = _experience.asStateFlow()

    private val _isOnboardingComplete: MutableStateFlow<Boolean> =
        MutableStateFlow(userSettingsRepository.isOnboardingComplete())
    val isOnboardingComplete: StateFlow<Boolean> = _isOnboardingComplete.asStateFlow()

    init {
        overrides.language?.let { appLocale.setLocale(it) }
        overrides.experience?.let { userSettingsRepository.saveExperience(it) }

        viewModelScope.launch {
            if (isOnDesktop()) {
                appLocale.setLocale(_appGlobalLanguage.value)
            } else if (isOnAndroid() && !appLocale.hasUserEverSetLanguage()) {
                if (_appGlobalLanguage.value == AppLang.DEFAULT) {
                    appLocale.setLocale(AppLang.DEFAULT)
                } else {
                    changeAppGlobalLanguage(AppLang.DEFAULT)
                }
            }
        }
    }

    fun changeAppGlobalLanguage(newLang: AppLang) {
        appLocale.setLocale(newLang)
        _appGlobalLanguage.value = newLang
    }

    fun changeThemeMode(newMode: ThemeMode) {
        userSettingsRepository.saveThemeMode(newMode)
        _themeMode.value = newMode
    }

    fun changeExperience(newExperience: Experience) {
        userSettingsRepository.saveExperience(newExperience)
        _experience.value = newExperience
        // Add platform-specific launcher icon synchronization (non-immediate by default)
        getPlatform().syncLauncherIcon(newExperience)
    }

    fun completeOnboarding() {
        userSettingsRepository.saveOnboardingComplete(true)
        _isOnboardingComplete.value = true
    }
}
