package com.turskyi.malaknyzhka.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontStyle
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.turskyi.malaknyzhka.MainActivity
import com.turskyi.malaknyzhka.R
import com.turskyi.malaknyzhka.infrastructure.RandomVerseDataProvider
import com.turskyi.malaknyzhka.models.RandomVerseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RandomVerseWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = withContext(Dispatchers.Default) {
            RandomVerseDataProvider.getRandomVerseData()
        }

        provideContent {
            GlanceTheme {
                WidgetContent(context, data)
            }
        }
    }

    @Composable
    private fun WidgetContent(context: Context, data: RandomVerseData?) {
        val size = LocalSize.current
        val availableHeight = size.height.value - 60
        val dynamicMaxLines = maxOf(3, (availableHeight / 18).toInt())

        val pageNumber = data?.pageNumber ?: 1
        val intent = Intent(
            context,
            MainActivity::class.java,
        ).apply {
            action = Intent.ACTION_VIEW
            this.data = "malaknyzhka://page/$pageNumber".toUri()
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        }

        // Deeper sepia warm tan background: #EFE9DF (day) / #26231F (night)
        val backgroundColor = ColorProvider(
            day = Color(0xFFEFE9DF),
            night = Color(0xFF26231F)
        )
        // Dark brown ink text/accent: #3D2C1E (day) / #F0E6D8 (night)
        val darkBrownColor = ColorProvider(
            day = Color(0xFF3D2C1E),
            night = Color(0xFFF0E6D8)
        )
        // Muted brown for footer
        val mutedBrownColor = ColorProvider(
            day = Color(0xFF5C4735),
            night = Color(0xFFC8B9A6)
        )

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(backgroundColor)
                .clickable(actionStartActivity(intent))
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Header: "Випадковий вірш" (No ink blue anywhere)
                Text(
                    text = context.getString(R.string.random_verse_widget_name),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = darkBrownColor
                    )
                )

                Spacer(GlanceModifier.size(4.dp))

                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(mutedBrownColor)
                ) {}

                Spacer(GlanceModifier.size(6.dp))

                val snippetText = data?.snippet ?: "..."
                // Hero: Snippet text, large, italic, enclosed in quote marks “...”
                Text(
                    text = "“$snippetText”",
                    style = TextStyle(
                        fontStyle = FontStyle.Italic,
                        fontSize = 13.sp,
                        color = darkBrownColor
                    ),
                    maxLines = dynamicMaxLines
                )

                Spacer(GlanceModifier.defaultWeight())

                // Footer: Small muted footer label "Сторінка {N}"
                Text(
                    text = context.getString(R.string.widget_page_label, pageNumber),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = mutedBrownColor
                    )
                )
            }
        }
    }
}
