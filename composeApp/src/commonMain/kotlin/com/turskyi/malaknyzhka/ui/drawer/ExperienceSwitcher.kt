package com.turskyi.malaknyzhka.ui.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = stringResource(Res.string.switch_experience),
            style = MaterialTheme.typography.caption,
            color = Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExperienceOption(
                text = stringResource(Res.string.experience_book),
                selected = currentExperience == Experience.BOOK,
                onClick = { onExperienceChange(Experience.BOOK) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ExperienceOption(
                text = stringResource(Res.string.experience_taras),
                selected = currentExperience == Experience.TARAS,
                onClick = { onExperienceChange(Experience.TARAS) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ExperienceOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = Color.White.copy(alpha = 0.6f)
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = Color.White,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
