package com.turskyi.malaknyzhka.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
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
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>()),
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
                    maxLines = 3
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
