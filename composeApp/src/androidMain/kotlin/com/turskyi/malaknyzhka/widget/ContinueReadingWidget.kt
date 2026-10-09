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
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.turskyi.malaknyzhka.MainActivity
import com.turskyi.malaknyzhka.R
import com.turskyi.malaknyzhka.createSettings
import com.turskyi.malaknyzhka.infrastructure.WidgetDataProvider
import com.turskyi.malaknyzhka.models.SettingsBookRepository
import com.turskyi.malaknyzhka.models.WidgetData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContinueReadingWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val settings = createSettings(context)
        val bookRepository = SettingsBookRepository(settings)
        val data = withContext(Dispatchers.Default) {
            WidgetDataProvider.getWidgetData(bookRepository)
        }

        provideContent {
            GlanceTheme {
                WidgetContent(context, data)
            }
        }
    }

    @Composable
    private fun WidgetContent(context: Context, data: WidgetData?) {
        val size = LocalSize.current
        val availableHeight = size.height.value - 70
        val dynamicMaxLines = maxOf(1, (availableHeight / 18).toInt())

        val intent = Intent(
            context,
            MainActivity::class.java,
        ).apply {
            if (data != null) {
                action = Intent.ACTION_VIEW
                this.data = "malaknyzhka://page/${data.pageNumber}".toUri()
            }
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        }

        // Pale cool notebook paper background: #F4F5F7 (day) / #16191E (night)
        val backgroundColor = ColorProvider(
            day = Color(0xFFF4F5F7),
            night = Color(0xFF16191E)
        )
        // Deep fountain-pen ink blue accent: #1B365D (day) / #60A5FA (night)
        val inkBlueColor = ColorProvider(
            day = Color(0xFF1B365D),
            night = Color(0xFF60A5FA)
        )
        // Dark ink body text: #2B2D31 (day) / #E2E8F0 (night)
        val darkInkColor = ColorProvider(
            day = Color(0xFF2B2D31),
            night = Color(0xFFE2E8F0)
        )

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(backgroundColor)
                .clickable(actionStartActivity(intent))
        ) {
            Row(
                modifier = GlanceModifier.fillMaxSize()
            ) {
                // Subtle vertical spine bar along the left edge (4dp width, max height)
                Box(
                    modifier = GlanceModifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(inkBlueColor)
                ) {}

                Column(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = context.getString(R.string.app_name),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = inkBlueColor
                            )
                        )
                    }

                    Spacer(GlanceModifier.size(4.dp))

                    // Single thin ink-blue rule under header
                    Box(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(inkBlueColor)
                    ) {}

                    Spacer(GlanceModifier.size(6.dp))

                    if (data != null) {
                        // Page number as hero (largest element)
                        Text(
                            text = context.getString(
                                R.string.widget_page_label,
                                data.pageNumber
                            ),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = darkInkColor
                            )
                        )

                        Spacer(GlanceModifier.size(4.dp))

                        Text(
                            text = data.excerpt,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = darkInkColor
                            ),
                            maxLines = dynamicMaxLines
                        )
                    } else {
                        Text(
                            text = context.getString(R.string.widget_continue_reading),
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = darkInkColor
                            )
                        )
                        Spacer(GlanceModifier.size(2.dp))
                        Text(
                            text = context.getString(R.string.widget_open_app),
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = darkInkColor
                            )
                        )
                    }
                }
            }
        }
    }
}
