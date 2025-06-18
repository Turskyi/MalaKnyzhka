package com.turskyi.malaknyzhka.ui.language

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun LanguageChip(
    label: String,
    icon: DrawableResource?,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor: Color = if (selected)
        MaterialTheme.colors.primary
    else
        Color.LightGray

    val textColor: Color = if (selected)
        Color.White
    else
        Color.Black

    Box(
        modifier = Modifier
            .background(
                backgroundColor,
                shape = RoundedCornerShape(50),
            )
            .clickable { onClick() }
            .padding(
                horizontal = 10.dp,
                vertical = 8.dp,
            )
    ) {
        if (icon != null)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(icon),
                // Basic accessibility, consider a dedicated string resource.
                contentDescription = "$label flag",
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = textColor,
                style = MaterialTheme.typography.button,
            )
        }
        else
            Text(
                text = label,
                color = textColor,
                style = MaterialTheme.typography.button,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
    }
}