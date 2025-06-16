package com.turskyi.malaknyzhka.ui.language

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.AppLang
import org.jetbrains.compose.resources.stringResource

@Composable
fun LanguageSwitcher(
    currentLanguage: AppLang,
    onLanguageChange: (AppLang) -> Unit
) {
    Column {
        Text(
            text = "Мова / Language",
            fontWeight = Bold,
            color = MaterialTheme.colors.surface,
            style = MaterialTheme.typography.h2,
        )
        Spacer(Modifier.height(8.dp))
        Row {
            LanguageChip(
                label = stringResource(AppLang.Ukraine.stringRes),
                icon = AppLang.Ukraine.imageRes,
                selected = currentLanguage == AppLang.Ukraine,
                onClick = { onLanguageChange(AppLang.Ukraine) }
            )
            Spacer(Modifier.width(8.dp))
            LanguageChip(
                label = stringResource(AppLang.English.stringRes),
                icon = AppLang.English.imageRes,
                selected = currentLanguage == AppLang.English,
                onClick = { onLanguageChange(AppLang.English) }
            )
        }
    }
}