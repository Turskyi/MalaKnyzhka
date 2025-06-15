package com.turskyi.malaknyzhka.ui.book

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.turskyi.malaknyzhka.models.PageSettings
import com.turskyi.malaknyzhka.models.WindowInfo
import com.turskyi.malaknyzhka.util.rememberWindowSize
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.about_app
import malaknyzhka.composeapp.generated.resources.cover
import malaknyzhka.composeapp.generated.resources.cover_description
import malaknyzhka.composeapp.generated.resources.menu
import malaknyzhka.composeapp.generated.resources.privacy_policy
import malaknyzhka.composeapp.generated.resources.support
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun Page(
    pageSettings: PageSettings,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToAbout: () -> Unit,
) {
    var isDrawerOpen by remember { mutableStateOf(false) }
    val initialPositionInTheMiddle = 0.5f
    var dividerPosition: Float by remember {
        mutableStateOf(
            initialPositionInTheMiddle
        )
    }

    // Constants for divider limits.
    val maxTopFraction = 0.025f
    val minBottomFraction = 0.94f

    // State for current page initialized with value from settings.
    var currentPage: Int by remember {
        mutableStateOf(pageSettings.getCurrentPage())
    }

    // Function to handle page changes.
    fun onNewPage(newPage: Int) {
        pageSettings.saveCurrentPage(newPage)
        currentPage = newPage
    }

    // Screen width detection.
    val windowInfo: WindowInfo = rememberWindowSize()

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize()
                .background(Color.White)
        ) {
            BookSpreads(
                dividerPosition = dividerPosition,
                currentPage = currentPage,
                onDividerPositionChange = { newPosition: Float ->
                    dividerPosition = newPosition.coerceIn(
                        maxTopFraction,
                        minBottomFraction,
                    )
                },
                screenWidth = windowInfo.screenWidth
            )

            // 📖 Bottom page switcher.
            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {
                PageSwitcherButtons(
                    currentPage = currentPage,
                    onPageChange = ::onNewPage,
                )
            }

            // 🍔 Burger button in top-left corner.
            IconButton(
                onClick = { isDrawerOpen = true },
                modifier = Modifier
                    .padding(4.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.4f),
                        shape = CircleShape
                    ).size(32.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.menu),
                    contentDescription = stringResource(Res.string.menu),
                    tint = MaterialTheme.colors.primary
                )
            }

            // 🪟 Semi-transparent overlay.
            if (isDrawerOpen) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { isDrawerOpen = false }
                )
            }

// Desired duration. 700 milliseconds.
            val customAnimationDurationMillis = 700

            // 📂 Drawer Panel.
            AnimatedVisibility(
                visible = isDrawerOpen,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = customAnimationDurationMillis)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = customAnimationDurationMillis)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = customAnimationDurationMillis)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = customAnimationDurationMillis)
                )
            ) {
                Box(
                    modifier = Modifier
                        .width(280.dp)
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
                                    strokeWidth = borderWidth.toPx()
                                )
                            }
                    )

                    Column(
                        Modifier
                            .width(280.dp)
                            .fillMaxHeight()
                            .background(Color.Black.copy(alpha = 0.4f))
                            .shadow(1.dp)
                            .padding(16.dp)
                    ) {

                        Spacer(Modifier.height(24.dp))

                        // ℹ️ About app.
                        Button(
                            onClick = {
                                isDrawerOpen = false
                                onNavigateToAbout()

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                            )

                        ) {
                            Text(
                                text = stringResource(Res.string.about_app),
                                color = Color.White,
                            )
                        }

                        // 🔗 Privacy Policy.
                        Button(
                            onClick = {
                                isDrawerOpen = false
                                onNavigateToPrivacyPolicy()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                            )
                        ) {
                            Text(
                                text = stringResource(Res.string.privacy_policy),
                                color = Color.White
                            )
                        }

                        // 🔧 Support.
                        Button(
                            onClick = {
                                isDrawerOpen = false
                                onNavigateToSupport()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                            )
                        ) {
                            Text(
                                text = stringResource(Res.string.support),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
