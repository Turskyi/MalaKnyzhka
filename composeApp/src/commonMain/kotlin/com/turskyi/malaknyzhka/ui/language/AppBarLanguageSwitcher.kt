package com.turskyi.malaknyzhka.ui.language

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.LocalWindowInfo
import com.turskyi.malaknyzhka.models.WindowInfo
import com.turskyi.malaknyzhka.ui.LocalAppLanguage
import com.turskyi.malaknyzhka.ui.LocalChangeAppLanguage
import com.turskyi.malaknyzhka.usecases.isOnIos
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBarLanguageSwitcher() {
    val currentLanguage: AppLang = LocalAppLanguage.current
    val onLanguageChange: (AppLang) -> Unit = LocalChangeAppLanguage.current
    val windowInfo: WindowInfo = LocalWindowInfo.current
    val isNarrow: Boolean = windowInfo.screenWidth < 600.dp

    Row(
        modifier = Modifier.padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isOnIos() || isNarrow) {
            LanguageChip(
                label = currentLanguage.shortName,
                icon = currentLanguage.imageRes,
                selected = true,
                onClick = { onLanguageChange(currentLanguage.toggle()) },
            )
        } else {
            LanguageChip(
                label = stringResource(AppLang.Ukraine.stringRes),
                icon = AppLang.Ukraine.imageRes,
                selected = currentLanguage == AppLang.Ukraine,
                onClick = { onLanguageChange(AppLang.Ukraine) },
            )
            Spacer(Modifier.width(8.dp))
            LanguageChip(
                label = stringResource(AppLang.English.stringRes),
                icon = AppLang.English.imageRes,
                selected = currentLanguage == AppLang.English,
                onClick = { onLanguageChange(AppLang.English) },
            )
        }
    }
}
