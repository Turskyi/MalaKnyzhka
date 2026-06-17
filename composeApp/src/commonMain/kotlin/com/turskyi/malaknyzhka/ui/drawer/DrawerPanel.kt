package com.turskyi.malaknyzhka.ui.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.AppLang
import com.turskyi.malaknyzhka.models.ThemeMode
import com.turskyi.malaknyzhka.ui.language.LanguageSwitcher
import com.turskyi.malaknyzhka.ui.theme.ThemeSwitcher
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.about_app
import malaknyzhka.composeapp.generated.resources.bookmarks
import malaknyzhka.composeapp.generated.resources.cover
import malaknyzhka.composeapp.generated.resources.cover_description
import malaknyzhka.composeapp.generated.resources.privacy_policy
import malaknyzhka.composeapp.generated.resources.support
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DrawerPanel(
    visible: Boolean,
    onClose: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToBookmarks: () -> Unit,
    currentLanguage: AppLang,
    onLanguageChange: (AppLang) -> Unit,
    currentThemeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
) {
    // Desired duration. 700 milliseconds.
    val customAnimationDurationMillis = 700

    // 📂 Drawer Panel.
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(durationMillis = customAnimationDurationMillis)
        ) + fadeIn(
            animationSpec = tween(customAnimationDurationMillis),
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(durationMillis = customAnimationDurationMillis)
        ) + fadeOut(
            animationSpec = tween(customAnimationDurationMillis),
        )
    ) {
        val width: Dp = 312.dp

        Box(
            modifier = Modifier
                .width(width)
                .fillMaxHeight()
        ) {
            // 🖼️ Background image.

            val borderWidth: Dp = 2.dp
            val borderColor: Color = Color.Black

            Image(
                painter = painterResource(Res.drawable.cover),
                contentDescription = stringResource(Res.string.cover_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = borderColor,
                            start = Offset(
                                size.width - borderWidth.toPx() / 2,
                                0f
                            ),
                            end = Offset(
                                size.width - borderWidth.toPx() / 2,
                                size.height
                            ),
                            strokeWidth = borderWidth.toPx(),
                        )
                    },
            )

            Column(
                Modifier
                    .width(width)
                    .fillMaxHeight()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .shadow(1.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(24.dp))

                // 🔖 Bookmarks.
                DrawerButton(
                    text = stringResource(Res.string.bookmarks),
                    onClick = {
                        onClose()
                        onNavigateToBookmarks()
                    },
                )

                // ℹ️ About app.
                DrawerButton(
                    text = stringResource(Res.string.about_app),
                    onClick = {
                        onClose()
                        onNavigateToAbout()
                    },
                )

                // 🔗 Privacy Policy.
                DrawerButton(
                    text = stringResource(Res.string.privacy_policy),
                    onClick = {
                        onClose()
                        onNavigateToPrivacyPolicy()
                    },
                )

                // 🔧 Support.
                DrawerButton(
                    text = stringResource(Res.string.support),
                    onClick = {
                        onClose()
                        onNavigateToSupport()
                    },
                )

                Spacer(modifier = Modifier.height(32.dp))

                LanguageSwitcher(
                    currentLanguage = currentLanguage,
                    onLanguageChange = onLanguageChange
                )

                Spacer(modifier = Modifier.height(16.dp))

                ThemeSwitcher(
                    currentThemeMode = currentThemeMode,
                    onThemeChange = onThemeChange
                )
            }
        }
    }
}
