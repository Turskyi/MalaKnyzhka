package com.turskyi.malaknyzhka.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.AppLocale
import com.turskyi.malaknyzhka.models.ThemeMode
import com.turskyi.malaknyzhka.models.UserSettingsRepository
import com.turskyi.malaknyzhka.usecases.isOnAndroid
import com.turskyi.malaknyzhka.usecases.isOnDesktop
import com.turskyi.malaknyzhka.usecases.toApLang
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val appLocale: AppLocale,
    private val userSettingsRepository: UserSettingsRepository,
) : ViewModel() {
    private val _appGlobalLanguage: MutableStateFlow<AppLang> =
        MutableStateFlow(appLocale.getLocale().toApLang())
    val appGlobalLanguage: StateFlow<AppLang> = _appGlobalLanguage.asStateFlow()

    private val _themeMode: MutableStateFlow<ThemeMode> =
        MutableStateFlow(userSettingsRepository.getThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    init {
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
}
