package com.turskyi.malaknyzhka.ui.privacy

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.ui.language.AppBarLanguageSwitcher
import com.turskyi.malaknyzhka.util.isOnWeb
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.arrow_back
import malaknyzhka.composeapp.generated.resources.back_button_description
import malaknyzhka.composeapp.generated.resources.logo
import malaknyzhka.composeapp.generated.resources.logo_description
import malaknyzhka.composeapp.generated.resources.privacy
import malaknyzhka.composeapp.generated.resources.privacy_policy
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Displays the privacy policy of the application.
 *
 * Note on iOS UI: The [TopAppBar] may appear significantly taller on iPhone
 * than on other platforms. This is due to Compose Multiplatform's default
 * edge-to-edge behavior on iOS.
 * On modern iPhones, the status bar inset (retrieved via
 * `WindowInsets.statusBars`) can be up to 54dp, which is added to the standard
 * Material 2 bar height of 56dp, resulting in a total height of ~110dp.
 *
 * In contrast, on Android (without edge-to-edge enabled), Web, and Desktop,
 * the status bar inset is 0, keeping the bar at its standard 56dp height.
 *
 * @param onBack Callback to navigate back to the previous screen.
 */
@Composable
fun PrivacyPolicyPage(onBack: () -> Unit) {
    val scrollState: ScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        text = stringResource(Res.string.privacy_policy),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
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
                },
                actions = {
                    AppBarLanguageSwitcher()
                },
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

            SelectionContainer {
                Text(text = stringResource(Res.string.privacy))
            }
        }
    }
}
