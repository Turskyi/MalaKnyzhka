package com.turskyi.malaknyzhka.ui.support

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.AppConstants
import com.turskyi.malaknyzhka.AppConstants.CHAT_CHANNEL
import com.turskyi.malaknyzhka.CHAT_LINK_TAG
import com.turskyi.malaknyzhka.EMAIL_LINK_TAG
import com.turskyi.malaknyzhka.SUPPORT_LINK_TAG
import com.turskyi.malaknyzhka.util.isOnWeb
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.app_allows
import malaknyzhka.composeapp.generated.resources.app_overview
import malaknyzhka.composeapp.generated.resources.arrow_back
import malaknyzhka.composeapp.generated.resources.back_button_description
import malaknyzhka.composeapp.generated.resources.can_change_font_size
import malaknyzhka.composeapp.generated.resources.chat_option
import malaknyzhka.composeapp.generated.resources.developer_support_page
import malaknyzhka.composeapp.generated.resources.email_option
import malaknyzhka.composeapp.generated.resources.faq
import malaknyzhka.composeapp.generated.resources.has_bookmarks
import malaknyzhka.composeapp.generated.resources.how_to_use
import malaknyzhka.composeapp.generated.resources.if_you_have_questions
import malaknyzhka.composeapp.generated.resources.is_maintained
import malaknyzhka.composeapp.generated.resources.logo
import malaknyzhka.composeapp.generated.resources.logo_description
import malaknyzhka.composeapp.generated.resources.page_saving
import malaknyzhka.composeapp.generated.resources.size_changing
import malaknyzhka.composeapp.generated.resources.support
import malaknyzhka.composeapp.generated.resources.thanks_for_using
import malaknyzhka.composeapp.generated.resources.turn_pages
import malaknyzhka.composeapp.generated.resources.we_appreciate
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
                        text = stringResource(Res.string.support),
                        modifier = Modifier.padding(horizontal = 8.dp),
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

            Text(
                text = stringResource(Res.string.app_overview),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(stringResource(Res.string.app_allows))

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.how_to_use),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(stringResource(Res.string.turn_pages))
            Text(stringResource(Res.string.size_changing))
            Text(stringResource(Res.string.page_saving))

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.faq),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(stringResource(Res.string.can_change_font_size))
            Text(stringResource(Res.string.has_bookmarks))
            Text(stringResource(Res.string.is_maintained))

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(Res.string.thanks_for_using))

            Spacer(modifier = Modifier.padding(8.dp))

            Text(text = stringResource(Res.string.if_you_have_questions))

            Spacer(modifier = Modifier.padding(8.dp))

            SelectionContainer {
                Column {
                    val annotatedEmailString: AnnotatedString =
                        buildAnnotatedString {
                            append(stringResource(Res.string.email_option))
                            pushStringAnnotation(
                                tag = EMAIL_LINK_TAG,
                                annotation = "mailto:${AppConstants.SUPPORT_EMAIL}"
                            )
                            withStyle(
                                style = SpanStyle(
                                    color = androidx.compose.material.MaterialTheme.colors.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(AppConstants.SUPPORT_EMAIL)
                            }
                            pop()
                        }
                    @Suppress("DEPRECATION")
                    ClickableText(
                        text = annotatedEmailString,
                        onClick = { offset: Int ->
                            annotatedEmailString.getStringAnnotations(
                                tag = EMAIL_LINK_TAG,
                                start = offset,
                                end = offset
                            ).firstOrNull()
                                ?.let { annotation: AnnotatedString.Range<String> ->
                                    uriHandler.openUri(annotation.item)
                                }
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    val annotatedString: AnnotatedString =
                        buildAnnotatedString {
                            append(stringResource(Res.string.chat_option))
                            pushStringAnnotation(
                                tag = CHAT_LINK_TAG,
                                annotation = CHAT_CHANNEL
                            )
                            withStyle(
                                style = SpanStyle(
                                    color = androidx.compose.material.MaterialTheme.colors.primary,
                                    textDecoration = TextDecoration.Underline,
                                )
                            ) {
                                append(CHAT_CHANNEL)
                            }
                            pop()
                        }
                    @Suppress("DEPRECATION")
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset: Int ->
                            annotatedString.getStringAnnotations(
                                tag = CHAT_LINK_TAG,
                                start = offset,
                                end = offset
                            ).firstOrNull()
                                ?.let { annotation: AnnotatedString.Range<String> ->
                                    uriHandler.openUri(annotation.item)
                                }
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    val annotatedSupportString: AnnotatedString =
                        buildAnnotatedString {
                            append(stringResource(Res.string.developer_support_page))
                            pushStringAnnotation(
                                tag = SUPPORT_LINK_TAG,
                                annotation = "https://${AppConstants.DEVELOPER_SUPPORT_PAGE}"
                            )
                            withStyle(
                                style = SpanStyle(
                                    color = androidx.compose.material.MaterialTheme.colors.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(AppConstants.DEVELOPER_SUPPORT_PAGE)
                            }
                            pop()
                        }
                    @Suppress("DEPRECATION")
                    ClickableText(
                        text = annotatedSupportString,
                        onClick = { offset: Int ->
                            annotatedSupportString.getStringAnnotations(
                                tag = SUPPORT_LINK_TAG,
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

            Text(text = stringResource(Res.string.we_appreciate))

            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}