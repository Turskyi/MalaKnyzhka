package com.turskyi.malaknyzhka.ui.drawer

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
import com.turskyi.malaknyzhka.models.Experience
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.experience_book
import malaknyzhka.composeapp.generated.resources.experience_taras
import malaknyzhka.composeapp.generated.resources.switch_experience
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExperienceSwitcher(
    currentExperience: Experience,
    onExperienceChange: (Experience) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                // Semi-transparent black background.
                color = Color.Black.copy(alpha = 0.8f),
                shape = RoundedCornerShape(16.dp),
            )
            .padding(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.switch_experience),
            style = MaterialTheme.typography.caption,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        ExperienceOption(
            text = stringResource(Res.string.experience_book),
            selected = currentExperience == Experience.BOOK,
            onClick = { onExperienceChange(Experience.BOOK) }
        )
        ExperienceOption(
            text = stringResource(Res.string.experience_taras),
            selected = currentExperience == Experience.TARAS,
            onClick = { onExperienceChange(Experience.TARAS) }
        )
    }
}

@Composable
private fun ExperienceOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
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
