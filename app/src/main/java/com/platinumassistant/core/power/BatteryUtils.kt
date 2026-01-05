package com.platinumassistant.core.power

import android.content.Context
import android.content.Intent
import android.content.IntentFilter

object BatteryUtils {
    fun getBatteryPercent(context: Context): Int {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = context.registerReceiver(null, ifilter)
        val level = batteryStatus?.getIntExtra("level", -1) ?: -1
        val scale = batteryStatus?.getIntExtra("scale", -1) ?: -1
        if (level < 0 || scale <= 0) return 100
        return (level * 100) / scale
    }
}
