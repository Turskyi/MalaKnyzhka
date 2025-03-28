package com.turskyi.malaknyzhka.ui.support

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.about_app
import malaknyzhka.composeapp.generated.resources.logo
import malaknyzhka.composeapp.generated.resources.logo_description
import malaknyzhka.composeapp.generated.resources.support_ukr_eng
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SupportPage(onBack: () -> Unit) {
    val scrollState: ScrollState = rememberScrollState()
    val uriHandler: UriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.support_ukr_eng),
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                },
                navigationIcon = {
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(Res.string.about_app))
            Text("Додаток \"Мала Книжка (Тарас Шевченко)\" дозволяє переглядати оцифровану версію рукопису та його друкований текст.")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Як користуватися")
            Text("- Перегортання сторінок за допомогою кнопок \"<\" і \">\".")
            Text("- Зміна розміру доступна лише для рукописних сторінок.")
            Text("- Додаток автоматично запам’ятовує останню переглянуту сторінку.")

            Spacer(modifier = Modifier.height(16.dp))

            Text("Часті запитання")
            Text("- Чи можна змінювати розмір друкованого тексту? Ні, наразі він фіксований.")
            Text("- Чи можна додавати власні закладки? Додаток автоматично запам’ятовує останню сторінку.")
            Text("- Чи будуть оновлення? Так, плануються покращення функціоналу.")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Дякуємо, що використовуєте додаток \"Мала Книжка (Тарас Шевченко)\"!",
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = "Якщо у вас виникли запитання, пропозиції або ви зіткнулися з проблемою, ви можете звернутися за наступними каналами:",
            )

            Spacer(modifier = Modifier.padding(8.dp))

            SelectionContainer {
                Column {
                    val annotatedEmailString: AnnotatedString =
                        buildAnnotatedString {
                            append("• Електронна адреса: ")
                            pushStringAnnotation(
                                tag = "EMAIL_LINK",
                                annotation = "mailto:support@turskyi.com"
                            )
                            withStyle(
                                style = SpanStyle(
                                    color = androidx.compose.material.MaterialTheme.colors.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("support@turskyi.com")
                            }
                            pop()
                        }
                    @Suppress("DEPRECATION")
                    ClickableText(
                        text = annotatedEmailString,
                        onClick = { offset ->
                            annotatedEmailString.getStringAnnotations(
                                tag = "EMAIL_LINK",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { annotation ->
                                uriHandler.openUri(annotation.item)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    val annotatedString: AnnotatedString =
                        buildAnnotatedString {
                            append("• Чат для питань та пропозицій в телеграм: ")
                            pushStringAnnotation(
                                tag = "TELEGRAM_LINK",
                                annotation = "https://t.me/+L3fMZsx-yLdlMTk6"
                            )
                            withStyle(
                                style = SpanStyle(
                                    color = androidx.compose.material.MaterialTheme.colors.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("https://t.me/+L3fMZsx-yLdlMTk6")
                            }
                            pop()
                        }
                    @Suppress("DEPRECATION")
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset: Int ->
                            annotatedString.getStringAnnotations(
                                tag = "TELEGRAM_LINK",
                                start = offset,
                                end = offset
                            ).firstOrNull()
                                ?.let { annotation: AnnotatedString.Range<String> ->
                                    uriHandler.openUri(annotation.item)
                                }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.padding(16.dp))

            Text(
                text = "Ми цінуємо ваші відгуки та працюємо над покращенням додатку!",
            )
        }
    }
}