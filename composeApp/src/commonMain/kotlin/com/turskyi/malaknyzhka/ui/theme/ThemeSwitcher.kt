package com.turskyi.malaknyzhka.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.ThemeMode
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.theme_dark
import malaknyzhka.composeapp.generated.resources.theme_light
import malaknyzhka.composeapp.generated.resources.theme_system
import org.jetbrains.compose.resources.stringResource

@Composable
fun ThemeSwitcher(
    currentThemeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
) {
    Column(
        modifier = Modifier.background(
            // Semi-transparent black background.
            color = Color.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(16.dp),
        ).padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        ThemeOption(
            text = stringResource(Res.string.theme_system),
            selected = currentThemeMode == ThemeMode.SYSTEM,
            onClick = { onThemeChange(ThemeMode.SYSTEM) }
        )
        ThemeOption(
            text = stringResource(Res.string.theme_light),
            selected = currentThemeMode == ThemeMode.LIGHT,
            onClick = { onThemeChange(ThemeMode.LIGHT) }
        )
        ThemeOption(
            text = stringResource(Res.string.theme_dark),
            selected = currentThemeMode == ThemeMode.DARK,
            onClick = { onThemeChange(ThemeMode.DARK) }
        )
    }
}

@Composable
private fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = Color.White.copy(alpha = 0.6f)
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
