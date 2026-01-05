package com.platinumassistant.lite

import android.content.Context
import android.os.BatteryManager

/**
 * Small battery helper. Uses BatteryManager where available.
 */
object BatteryUtils {
    fun getBatteryPercent(ctx: Context): Int {
        return try {
            val bm = ctx.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
            if (level >= 0) level else -1
        } catch (_: Exception) {
            -1
        }
    }
}
