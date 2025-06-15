package com.turskyi.malaknyzhka.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.util.isOnWeb
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.about_app
import malaknyzhka.composeapp.generated.resources.about_app_ai_translation_info
import malaknyzhka.composeapp.generated.resources.about_app_description_part1
import malaknyzhka.composeapp.generated.resources.about_app_description_part2
import malaknyzhka.composeapp.generated.resources.about_app_little_book
import malaknyzhka.composeapp.generated.resources.about_app_no_alternatives
import malaknyzhka.composeapp.generated.resources.about_app_target_audience
import malaknyzhka.composeapp.generated.resources.about_app_thank_you_message
import malaknyzhka.composeapp.generated.resources.arrow_back
import malaknyzhka.composeapp.generated.resources.back_button_description
import malaknyzhka.composeapp.generated.resources.logo
import malaknyzhka.composeapp.generated.resources.logo_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutPage(onBack: () -> Unit) {
    val scrollState: ScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.about_app)) },
                navigationIcon = {
                    if (isOnWeb()) {
                        IconButton(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 2.dp,
                            ),
                            onClick = onBack
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.logo),
                                contentDescription = stringResource(
                                    Res.string.logo_description,
                                ),
                                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                            )
                        }
                    } else {
                        IconButton(onClick = onBack) {
                            Image(
                                painter = painterResource(Res.drawable.arrow_back),
                                contentDescription = stringResource(Res.string.back_button_description),
                                colorFilter = ColorFilter.tint(Color.White),
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(Res.string.about_app_little_book),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp, top = 16.dp)
            )

            Text(
                text = stringResource(Res.string.about_app_description_part1),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.about_app_description_part2),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.about_app_no_alternatives),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = stringResource(Res.string.about_app_ai_translation_info),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.about_app_target_audience),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.about_app_thank_you_message),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}