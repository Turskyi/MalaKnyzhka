package com.turskyi.malaknyzhka.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.chat_welcome_headline
import malaknyzhka.composeapp.generated.resources.chat_welcome_supporting_text
import malaknyzhka.composeapp.generated.resources.suggestion_1
import malaknyzhka.composeapp.generated.resources.suggestion_2
import malaknyzhka.composeapp.generated.resources.suggestion_3
import malaknyzhka.composeapp.generated.resources.taras_with_book
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatEmptyView(
    onSuggestionClick: (String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
            initialOffsetY = { 20 },
            animationSpec = tween(1000)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.sizeIn(maxWidth = 600.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.taras_with_book),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .sizeIn(maxHeight = 300.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(Res.string.chat_welcome_headline),
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.chat_welcome_supporting_text),
                    style = MaterialTheme.typography.body1.copy(
                        lineHeight = 24.sp,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val suggestions = listOf(
                        stringResource(Res.string.suggestion_1),
                        stringResource(Res.string.suggestion_2),
                        stringResource(Res.string.suggestion_3)
                    )

                    suggestions.forEach { suggestion ->
                        SuggestionChip(
                            label = suggestion,
                            onClick = { onSuggestionClick(suggestion) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colors.primary.copy(alpha = 0.08f),
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.button.copy(
                color = MaterialTheme.colors.primary,
                fontSize = 14.sp
            )
        )
    }
}
