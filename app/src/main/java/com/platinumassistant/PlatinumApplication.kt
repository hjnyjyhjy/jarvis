package com.platinumassistant

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application entry point for Platinum Arabic AI Assistant
 */
@HiltAndroidApp
class PlatinumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // In production, only log errors
            Timber.plant(CrashReportingTree())
        }
    }

    /**
     * Custom tree for crash reporting in production
     */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?
        ) {
            if (priority >= android.util.Log.ERROR) {
                // TODO: Send to crash reporting service
                Timber.tag(tag ?: "CrashReporting").e(t, message)
            }
        }
    }
}
