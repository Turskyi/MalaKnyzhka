package com.turskyi.malaknyzhka.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import malaknyzhka.composeapp.generated.resources.GetItOnGooglePlay_Badge_Web_color_Ukranian
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.about_app
import malaknyzhka.composeapp.generated.resources.arrow_forward
import malaknyzhka.composeapp.generated.resources.macos_badge
import malaknyzhka.composeapp.generated.resources.portrait_description
import malaknyzhka.composeapp.generated.resources.privacy_policy
import malaknyzhka.composeapp.generated.resources.read_now
import malaknyzhka.composeapp.generated.resources.screenshot_1
import malaknyzhka.composeapp.generated.resources.screenshot_2
import malaknyzhka.composeapp.generated.resources.screenshot_3
import malaknyzhka.composeapp.generated.resources.screenshot_4
import malaknyzhka.composeapp.generated.resources.screenshot_5
import malaknyzhka.composeapp.generated.resources.screenshot_6
import malaknyzhka.composeapp.generated.resources.screenshot_7
import malaknyzhka.composeapp.generated.resources.screenshot_8
import malaknyzhka.composeapp.generated.resources.screenshot_9
import malaknyzhka.composeapp.generated.resources.shevchenko_portrait
import malaknyzhka.composeapp.generated.resources.subtitle
import malaknyzhka.composeapp.generated.resources.support
import malaknyzhka.composeapp.generated.resources.test_flight_badge
import malaknyzhka.composeapp.generated.resources.title
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun LandingPage(
    onNavigateToBook: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    val uriHandler: UriHandler = LocalUriHandler.current
    val scrollState: ScrollState = rememberScrollState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                .padding(vertical = 8.dp), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(
                        vertical = 16.dp, horizontal = 24.dp
                    ).widthIn(min = 1000.dp, max = 2000.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(
                                Res.drawable.shevchenko_portrait,
                            ),
                            contentDescription = stringResource(
                                Res.string.portrait_description,
                            ),
                            modifier = Modifier.size(180.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(Res.string.title),
                            style = MaterialTheme.typography.h4,
                            color = MaterialTheme.colors.primaryVariant
                        )
                        Text(
                            text = stringResource(Res.string.subtitle),
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onSurface.copy(
                                alpha = 0.7f,
                            )
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Основні можливості",
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            listOf(
                                "• Читайте рукопис разом із друкованим текстом.",
                                "• Порівнюйте оригінал із сучасним написанням.",
                                "• Автоматичне збереження останньої прочитаної сторінки."
                            ).forEach { feature: String ->
                                Text(
                                    text = feature,
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface.copy(
                                        alpha = 0.8f,
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onNavigateToBook,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = stringResource(Res.string.read_now))
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // Reserve space for iPhone ScreenshotCarousel.
                        Spacer(modifier = Modifier.height(480.dp))

                        Spacer(modifier = Modifier.height(8.dp))

                        // Reserve space for iPad ScreenshotCarousel.
                        Spacer(modifier = Modifier.height(240.dp))

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Доступно для Android, iOS та macOS!",
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.primaryVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // 🏪 Google Play, TestFlight and MacOS Badges.
                        val badgeSpacing: Dp = 12.dp
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                        ) {
                            Image(
                                painter = painterResource(
                                    Res.drawable.GetItOnGooglePlay_Badge_Web_color_Ukranian,
                                ),
                                contentDescription = "Значок Google Play",
                                modifier = Modifier.clickable {
                                    uriHandler.openUri(
                                        uri = "https://play.google.com/store/apps/details?id=com.turskyi.malaknyzhka",
                                    )
                                }.weight(1f, fill = false).height(64.dp)
                                    .width(216.dp),
                                contentScale = ContentScale.Fit,
                            )
                            Spacer(modifier = Modifier.width(badgeSpacing))
                            Image(
                                painter = painterResource(
                                    Res.drawable.test_flight_badge,
                                ),
                                contentDescription = "Значок TestFlight",
                                modifier = Modifier.clickable {
                                    uriHandler.openUri(
                                        uri = "https://testflight.apple.com/join/cEN4y79T",
                                    )
                                }.weight(1f, fill = false).height(64.dp),
                                contentScale = ContentScale.Fit,
                            )
                            Spacer(modifier = Modifier.width(badgeSpacing))
                            Image(
                                painter = painterResource(
                                    Res.drawable.macos_badge,
                                ),
                                contentDescription = "Значок MacOS",
                                modifier = Modifier.clickable {
                                    uriHandler.openUri(
                                        uri = "https://github.com/Turskyi/MalaKnyzhka/releases/download/1.0.4(4)/mala_knyzhka.dmg",
                                    )
                                }.weight(1f, fill = false).height(64.dp),
                                contentScale = ContentScale.Fit,
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Джерела:",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(
                                    alpha = 0.6f
                                )
                            )
                            Spacer(Modifier.width(4.dp))
                            val url: String =
                                "https://www.t-shevchenko.name/uk/Gallery/" + "Works/1850MalaKn.html"
                            val annotatedLinkString: AnnotatedString =
                                buildAnnotatedString {
                                    pushStringAnnotation(
                                        tag = "URL", annotation = url
                                    )
                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colors.primary,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    ) {
                                        append(url)
                                    }
                                    pop()
                                }
                            SelectionContainer {
                                @Suppress("DEPRECATION") ClickableText(
                                    text = annotatedLinkString,
                                    style = MaterialTheme.typography.caption,
                                    onClick = { offset: Int ->
                                        annotatedLinkString.getStringAnnotations(
                                            tag = "URL",
                                            start = offset,
                                            end = offset
                                        ).firstOrNull()
                                            ?.let { annotation: AnnotatedString.Range<String> ->
                                                uriHandler.openUri(annotation.item)
                                            }
                                    })
                            }
                            Icon(
                                painter = painterResource(
                                    Res.drawable.arrow_forward,
                                ),
                                contentDescription = "Відкрити в новому вікні",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(16.dp).padding(
                                    start = 4.dp,
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(
                        text = stringResource(Res.string.privacy_policy),
                        style = MaterialTheme.typography.caption.copy(
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable {
                            onNavigateToPrivacyPolicy()
                        }.padding(16.dp)
                    )
                    Text(
                        text = stringResource(Res.string.support),
                        style = MaterialTheme.typography.caption.copy(
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable { onNavigateToSupport() }
                            .padding(16.dp),
                    )
                    Text(
                        text = stringResource(Res.string.about_app),
                        style = MaterialTheme.typography.caption.copy(
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable {
                            onNavigateToAbout()
                        }.padding(16.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(top = 220.dp),
            ) {
                val iPhoneScreenshots: List<DrawableResource> = listOf(
                    Res.drawable.screenshot_1,
                    Res.drawable.screenshot_2,
                    Res.drawable.screenshot_3,
                    Res.drawable.screenshot_4,
                )

                ScreenshotCarousel(
                    screenshots = iPhoneScreenshots,
                    ratio = 9f / 19.5f,
                    height = 480.dp,
                )

                Spacer(modifier = Modifier.height(8.dp))

                val iPadScreenshots: List<DrawableResource> = listOf(
                    Res.drawable.screenshot_5,
                    Res.drawable.screenshot_6,
                    Res.drawable.screenshot_7,
                    Res.drawable.screenshot_8,
                    Res.drawable.screenshot_9,
                )
                ScreenshotCarousel(
                    screenshots = iPadScreenshots,
                    ratio = 9f / 12,
                    height = 240.dp,
                )
            }
        }
    }
}