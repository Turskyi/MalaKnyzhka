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
import malaknyzhka.composeapp.generated.resources.about_taras_ask_desc
import malaknyzhka.composeapp.generated.resources.about_taras_ask_example1
import malaknyzhka.composeapp.generated.resources.about_taras_ask_example2
import malaknyzhka.composeapp.generated.resources.about_taras_ask_example3
import malaknyzhka.composeapp.generated.resources.about_taras_ask_example4
import malaknyzhka.composeapp.generated.resources.about_taras_ask_examples_title
import malaknyzhka.composeapp.generated.resources.about_taras_ask_footer
import malaknyzhka.composeapp.generated.resources.about_taras_ask_title
import malaknyzhka.composeapp.generated.resources.about_taras_education_desc1
import malaknyzhka.composeapp.generated.resources.about_taras_education_desc2
import malaknyzhka.composeapp.generated.resources.about_taras_education_title
import malaknyzhka.composeapp.generated.resources.about_taras_intro_part1
import malaknyzhka.composeapp.generated.resources.about_taras_intro_part2
import malaknyzhka.composeapp.generated.resources.about_taras_manuscripts_desc1
import malaknyzhka.composeapp.generated.resources.about_taras_manuscripts_desc2
import malaknyzhka.composeapp.generated.resources.about_taras_manuscripts_title
import malaknyzhka.composeapp.generated.resources.about_taras_privacy_desc1
import malaknyzhka.composeapp.generated.resources.about_taras_privacy_desc2
import malaknyzhka.composeapp.generated.resources.about_taras_privacy_title
import malaknyzhka.composeapp.generated.resources.about_taras_title
import malaknyzhka.composeapp.generated.resources.logo
import malaknyzhka.composeapp.generated.resources.logo_description
import malaknyzhka.composeapp.generated.resources.menu
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutTarasPage(
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
                        text = stringResource(Res.string.about_taras_title),
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
                                tint = MaterialTheme.colors.onPrimary
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
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_intro_part1),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_intro_part2),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_ask_title),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_ask_desc),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_ask_examples_title),
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Medium
                    )

                    Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp)) {
                        Text(
                            text = stringResource(Res.string.about_taras_ask_example1),
                            style = MaterialTheme.typography.body1
                        )
                        Text(
                            text = stringResource(Res.string.about_taras_ask_example2),
                            style = MaterialTheme.typography.body1
                        )
                        Text(
                            text = stringResource(Res.string.about_taras_ask_example3),
                            style = MaterialTheme.typography.body1
                        )
                        Text(
                            text = stringResource(Res.string.about_taras_ask_example4),
                            style = MaterialTheme.typography.body1
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_ask_footer),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_manuscripts_title),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_manuscripts_desc1),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_manuscripts_desc2),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_education_title),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_education_desc1),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_education_desc2),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_privacy_title),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_privacy_desc1),
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(Res.string.about_taras_privacy_desc2),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
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
