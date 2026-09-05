package com.turskyi.malaknyzhka.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.AppConstants
import malaknyzhka.composeapp.generated.resources.GetItOnGooglePlay_Badge_Web_color
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.app_store_badge
import malaknyzhka.composeapp.generated.resources.badge_app_store_description
import malaknyzhka.composeapp.generated.resources.badge_google_play_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppStoreBadges(modifier: Modifier = Modifier) {
    val uriHandler: UriHandler = LocalUriHandler.current
    Row(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                Res.drawable.GetItOnGooglePlay_Badge_Web_color,
            ),
            contentDescription = stringResource(
                Res.string.badge_google_play_description,
            ),
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    uriHandler.openUri(
                        uri = AppConstants.ANDROID_URI,
                    )
                },
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = painterResource(
                Res.drawable.app_store_badge,
            ),
            contentDescription = stringResource(
                Res.string.badge_app_store_description,
            ),
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    uriHandler.openUri(
                        uri = AppConstants.APP_STORE_URI,
                    )
                },
            contentScale = ContentScale.Fit,
        )
    }
}
