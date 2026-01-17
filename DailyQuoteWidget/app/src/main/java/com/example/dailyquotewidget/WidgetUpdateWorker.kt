package com.example.dailyquotewidget

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.state.preferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAppWidget
import com.example.dailyquotewidget.data.repository.QuoteRepository
import com.example.dailyquotewidget.widget.QuoteGlanceWidget
import dagger.hilt.work.HiltWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class WidgetUpdateWorker @Inject constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val repo: QuoteRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val quote = repo.getRandom()
            if (quote != null) {
                updateAppWidgetState(
                    applicationContext,
                    PreferencesGlanceStateDefinition,
                    QuoteGlanceWidget.KEY_QUOTE_TEXT
                ) { prefs -> prefs.toMutablePreferences().apply { this[preferencesKey<String>("widget_quote_text")] = quote.text } }

                updateAppWidgetState(
                    applicationContext,
                    PreferencesGlanceStateDefinition,
                    QuoteGlanceWidget.KEY_QUOTE_AUTHOR
                ) { prefs -> prefs.toMutablePreferences().apply { this[preferencesKey<String>("widget_quote_author")] = quote.author } }

                val manager = GlanceAppWidgetManager(applicationContext)
                val glanceIds = manager.getGlanceIds(QuoteGlanceWidget::class.java)
                updateAppWidget(applicationContext, QuoteGlanceWidget::class.java, glanceIds)
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}{
}