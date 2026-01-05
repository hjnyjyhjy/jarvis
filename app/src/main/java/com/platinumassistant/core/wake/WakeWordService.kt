package com.platinumassistant.core.wake

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Skeleton foreground service that will host a wake-word detector.
 * NOTE: This is a framework/skeleton. To get a real low-power wake-word you must
 * provide an implementation of `WakeWordDetector` (e.g. Porcupine) and initialize it
 * in `onCreate` or lazily when starting listening.
 */
class WakeWordService : Service() {
    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var detector: WakeWordDetector? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundCompat()
        Timber.i("WakeWordService created")
        // Initialize Vosk-based wake-word detector by default (open-source)
        try {
            // If models are placed under filesDir/models/wake, set this path in production
            val wakeModelPath = applicationContext.filesDir.absolutePath + "/models/wake"
            val detectorImpl = com.platinumassistant.core.wake.VoskWakeWordDetector()
            scope.launch {
                    try {
                        detectorImpl.initialize(wakeModelPath)
                        detector = detectorImpl
                    } catch (t: Throwable) {
                        Timber.e(t, "Failed to init VoskWakeWordDetector")
                    }
                }
        } catch (t: Throwable) {
            Timber.e(t, "Error initializing default wake detector: %s", t)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("WakeWordService onStartCommand: %s", intent)
        // start listening in background
        scope.launch {
            try {
                // Check power mode and battery level; if battery_saver and battery low skip starting
                var modeVal = "balanced"
                try {
                    modeVal = com.platinumassistant.core.prefs.PreferencesManager.getPowerMode(applicationContext).first()
                } catch (t: Throwable) {
                    Timber.w(t, "Could not read power mode, defaulting to balanced")
                }

                var skip = false
                if (modeVal == "battery_saver") {
                    try {
                        val perc = com.platinumassistant.core.power.BatteryUtils.getBatteryPercent(applicationContext)
                        if (perc < 20) {
                            skip = true
                            Timber.i("Battery low (%s%%) and in battery_saver mode, skipping wake detector", perc)
                        }
                    } catch (t: Throwable) {
                        Timber.e(t, "Error checking battery percent")
                    }
                }

                if (!skip) {
                    detector?.startListening(onDetected = {
                        Timber.i("Wake word detected!")
                        // Broadcast a wake detected intent so UI / app can react and start STT
                        try {
                            val intent = android.content.Intent("com.platinumassistant.ACTION_WAKE_DETECTED")
                            intent.setPackage(applicationContext.packageName)
                            applicationContext.sendBroadcast(intent)
                        } catch (t: Throwable) {
                            Timber.e(t, "Failed to broadcast wake event")
                        }
                    }, onError = { t ->
                        Timber.e(t, "Detector error")
                    })
                }
            } catch (t: Throwable) {
                Timber.e(t, "Failed starting detector")
            }
        }

        // Keep service alive unless explicitly stopped
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            detector?.stopListening()
        } catch (t: Throwable) {
            Timber.e(t, "Error stopping detector")
        }
        scope.cancel()
        Timber.i("WakeWordService destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundCompat() {
        val channelId = "wake_word_service"
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, "Wake-word Service", NotificationManager.IMPORTANCE_LOW)
            nm.createNotificationChannel(chan)
        }
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Platinum AI — استماع منخفض الطاقة")
            .setContentText("الاستماع لكلمة الاستيقاظ")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setOnlyAlertOnce(true)
            .build()

        startForeground(1337, notification)
    }
}
