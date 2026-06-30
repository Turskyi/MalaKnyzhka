package com.turskyi.malaknyzhka.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
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
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
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
        // Header height estimation:
        // Padding(12+12) + AppName(~20) + Spacer(8) + Page(~18) + Spacer(4) =
        // 62dp
        val availableHeight = size.height.value - 62
        // Line height for 13sp is approx 18dp
        val dynamicMaxLines = maxOf(1, (availableHeight / 18).toInt())

        val intent = if (data != null) {
            Intent(
                Intent.ACTION_VIEW,
                "malaknyzhka://page/${data.pageNumber}".toUri()
            ).apply {
                setPackage(context.packageName)
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP,
                )
            }
        } else {
            Intent(
                context,
                MainActivity::class.java,
            ).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP
                )
            }
        }

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .clickable(actionStartActivity(intent))
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Use a simple icon if available, or just text
                    Text(
                        text = context.getString(R.string.app_name),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = GlanceTheme.colors.primary
                        )
                    )
                }

                Spacer(GlanceModifier.size(8.dp))

                if (data != null) {
                    Text(
                        text = context.getString(
                            R.string.widget_page_label,
                            data.pageNumber
                        ),
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = GlanceTheme.colors.onSurface
                        )
                    )

                    Spacer(GlanceModifier.size(4.dp))

                    Text(
                        text = data.excerpt,
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = GlanceTheme.colors.onSurfaceVariant
                        ),
                        maxLines = dynamicMaxLines
                    )
                } else {
                    Text(
                        text = context.getString(R.string.widget_continue_reading),
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = GlanceTheme.colors.onSurface
                        )
                    )
                    Text(
                        text = context.getString(R.string.widget_open_app),
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = GlanceTheme.colors.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}
