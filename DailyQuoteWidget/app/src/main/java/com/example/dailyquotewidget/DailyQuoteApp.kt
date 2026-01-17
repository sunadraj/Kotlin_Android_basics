package com.example.dailyquotewidget

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class DailyQuoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Schedule daily widget update
        val request = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(0, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_widget_update",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}