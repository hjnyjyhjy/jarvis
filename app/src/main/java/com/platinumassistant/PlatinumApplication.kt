package com.platinumassistant

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application entry point for Platinum Arabic AI Assistant
 * 
 * Responsibilities:
 * - Initialize Hilt dependency injection
 * - Set up logging (Timber)
 * - Initialize global configurations
 * - Crash reporting setup
 * - Background services initialization
 */
@HiltAndroidApp
class PlatinumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize logging
        setupLogging()
        
        // TODO: Initialize other services
        // - Voice recognition engine
        // - AI models
        // - Notification manager
        // - Background worker
        // - Crash reporting
        
        Timber.i("Platinum Assistant Application initialized")
    }

    /**
     * Set up Timber logging for debug and production builds
     */
    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // In production, only log errors to crash reporting
            Timber.plant(CrashReportingTree())
        }
    }

    /**
     * Custom Timber tree for crash reporting in production
     * Sends critical errors to remote crash reporting service
     */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?
        ) {
            if (priority >= Log.ERROR) {
                // TODO: Send to crash reporting service (Firebase Crashlytics, etc.)
                Timber.tag(tag ?: "CrashReporting").e(t, message)
            }
        }
    }
}
