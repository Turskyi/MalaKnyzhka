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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import malaknyzhka.composeapp.generated.resources.GetItOnGooglePlay_Badge_Web_color_Ukranian
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.shevchenko_portrait
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun LandingPage(
    onNavigateToBook: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
) {
    val uriHandler: UriHandler = LocalUriHandler.current
    val scrollState: ScrollState = rememberScrollState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(
                                Res.drawable.shevchenko_portrait,
                            ),
                            contentDescription = "Портрет Тараса Шевченка",
                            modifier = Modifier
                                .size(180.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Мала Книжка",
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.primaryVariant
                        )
                        Text(
                            text = "Оригінал рукопису Тараса Шевченка",
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.onSurface.copy(
                                alpha = 0.7f,
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Додаток дозволяє читати рукопис разом із " +
                                    "друкованим текстом, порівнюючи оригінал з " +
                                    "сучасним текстом.",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onNavigateToBook,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Читати")
                        }

                        // Spacer and Google Play Badge
                        Spacer(modifier = Modifier.height(24.dp))
                        Image(
                            painter = painterResource(Res.drawable.GetItOnGooglePlay_Badge_Web_color_Ukranian),
                            contentDescription = "Значок Google Play",
                            modifier = Modifier
                                .clickable {
                                    uriHandler.openUri(
                                        uri = "https://play.google.com/store/apps/details?id=com.turskyi.malaknyzhka",
                                    )
                                }
                                .size(
                                    240.dp,
                                    64.dp,
                                )
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Джерела:",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(
                                    alpha = 0.6f
                                )
                            )
                            Spacer(Modifier.width(4.dp))
                            val url: String =
                                "https://www.t-shevchenko.name/uk/Gallery/" +
                                        "Works/1850MalaKn.html"
                            val annotatedLinkString: AnnotatedString =
                                buildAnnotatedString {
                                    pushStringAnnotation(
                                        tag = "URL",
                                        annotation = url
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
                                @Suppress("DEPRECATION")
                                ClickableText(
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
                                    }
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Відкрити в новому вікні",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Політика конфіденційності",
                    style = MaterialTheme.typography.caption.copy(
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable { onNavigateToPrivacyPolicy() }
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }
        }
    }
}