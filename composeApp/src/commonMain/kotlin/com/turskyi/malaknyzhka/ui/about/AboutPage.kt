package com.turskyi.malaknyzhka.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.Experience
import com.turskyi.malaknyzhka.models.ThemeMode
import com.turskyi.malaknyzhka.ui.LocalAppLanguage
import com.turskyi.malaknyzhka.ui.LocalChangeAppLanguage
import com.turskyi.malaknyzhka.ui.LocalChangeThemeMode
import com.turskyi.malaknyzhka.ui.LocalThemeMode
import com.turskyi.malaknyzhka.ui.drawer.DrawerPanel
import com.turskyi.malaknyzhka.ui.language.AppBarLanguageSwitcher
import com.turskyi.malaknyzhka.usecases.isOnWeb
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.about_app
import malaknyzhka.composeapp.generated.resources.about_app_ai_translation_info
import malaknyzhka.composeapp.generated.resources.about_app_description_part1
import malaknyzhka.composeapp.generated.resources.about_app_description_part2
import malaknyzhka.composeapp.generated.resources.about_app_linguistic_example
import malaknyzhka.composeapp.generated.resources.about_app_little_book
import malaknyzhka.composeapp.generated.resources.about_app_no_alternatives
import malaknyzhka.composeapp.generated.resources.about_app_search_description
import malaknyzhka.composeapp.generated.resources.about_app_search_title
import malaknyzhka.composeapp.generated.resources.about_app_target_audience
import malaknyzhka.composeapp.generated.resources.about_app_thank_you_message
import malaknyzhka.composeapp.generated.resources.data_storage_info
import malaknyzhka.composeapp.generated.resources.logo
import malaknyzhka.composeapp.generated.resources.logo_description
import malaknyzhka.composeapp.generated.resources.menu
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Displays information about the application.
 *
 * Note on iOS UI: The [TopAppBar] may appear significantly taller on iPhone
 * than on other platforms. This is due to Compose Multiplatform's default
 * edge-to-edge behaviour on iOS.
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
fun AboutPage(
    onBack: () -> Unit,
    currentExperience: Experience = Experience.TARAS,
    onExperienceChange: (Experience) -> Unit = {},
    showExperienceSwitcher: Boolean = false,
    onNavigateToBook: () -> Unit = {},
    onNavigateToBookmarks: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
) {
    val scrollState: ScrollState = rememberScrollState()

    val appGlobalLanguage: AppLang = LocalAppLanguage.current
    val changeAppGlobalLanguage: (AppLang) -> Unit =
        LocalChangeAppLanguage.current

    val currentThemeMode: ThemeMode = LocalThemeMode.current
    val onThemeChange: (ThemeMode) -> Unit = LocalChangeThemeMode.current

    var isDrawerOpen: Boolean by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        text = stringResource(Res.string.about_app),
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
                                modifier = Modifier.clip(
                                    RoundedCornerShape(10.dp),
                                )
                            )
                        }
                    } else {
                        IconButton(onClick = { isDrawerOpen = true }) {
                            Icon(
                                painter = painterResource(Res.drawable.menu),
                                contentDescription = stringResource(Res.string.menu),
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
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = stringResource(Res.string.about_app_little_book),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                    )

                    Text(
                        text = stringResource(
                            Res.string.about_app_description_part1,
                        ),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(
                            Res.string.about_app_description_part2,
                        ),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(Res.string.about_app_no_alternatives),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(
                            Res.string.about_app_ai_translation_info,
                        ),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(Res.string.about_app_target_audience),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(Res.string.about_app_search_title),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = stringResource(
                            Res.string.about_app_search_description,
                        ),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(
                            Res.string.about_app_linguistic_example,
                        ),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(
                            Res.string.about_app_thank_you_message,
                        ),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(Res.string.data_storage_info),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // 🪟 Semi-transparent overlay for drawer.
            if (isDrawerOpen) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { isDrawerOpen = false }
                )
            }

            DrawerPanel(
                visible = isDrawerOpen,
                onClose = { isDrawerOpen = false },
                onNavigateToBookmarks = {
                    onNavigateToBookmarks()
                    isDrawerOpen = false
                },
                onNavigateToBook = {
                    onNavigateToBook()
                    isDrawerOpen = false
                },
                onNavigateToPrivacyPolicy = {
                    onNavigateToPrivacyPolicy()
                    isDrawerOpen = false
                },
                onNavigateToSupport = {
                    onNavigateToSupport()
                    isDrawerOpen = false
                },
                onNavigateToAbout = {
                    onNavigateToAbout()
                    isDrawerOpen = false
                },
                onNavigateToChat = {
                    onNavigateToChat()
                    isDrawerOpen = false
                },
                currentExperience = currentExperience,
                onExperienceChange = { experience ->
                    onExperienceChange(experience)
                    isDrawerOpen = false
                },
                showExperienceSwitcher = showExperienceSwitcher,
                currentLanguage = appGlobalLanguage,
                onLanguageChange = {
                    changeAppGlobalLanguage(it)
                    isDrawerOpen = false
                },
                currentThemeMode = currentThemeMode,
                onThemeChange = {
                    onThemeChange(it)
                    isDrawerOpen = false
                }
            )
        }
    }
}