package com.platinumassistant.lite

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Tiny rule-based command parser.
 * - One responsibility: map text -> action string
 * - Keep rules simple and readable (no complex NLP)
 */
object CommandParser {
    // Helper pure function for easier testing
    fun normalizeInput(text: String): String = text.trim().lowercase()
    fun parseAndExecute(ctx: Context, text: String): String {
        val input = normalizeInput(text)

        // tiny helper to detect Arabic characters and switch responses accordingly
        fun containsArabic(s: String) = s.any { it in '\u0600'..'\u06FF' }
        val arabic = containsArabic(text)

        return when {
            // open / افتح
            input.startsWith("open") || input.startsWith("افتح") || input.startsWith("فتح") -> {
                val target = if (input.startsWith("open")) input.removePrefix("open").trim() else input.replaceFirst(Regex("^(افتح|فتح)"), "").trim()
                openAppOrSettings(ctx, target)
            }

            // battery / البطارية
            input.contains("battery") || input.contains("البطارية") -> {
                val pct = BatteryUtils.getBatteryPercent(ctx)
                if (arabic) "مستوى البطارية $pct%" else "Battery at $pct%"
            }

            // wifi
            input.contains("toggle wifi") || input.contains("wifi") || input.contains("واي فاي") || input.contains("وايفاي") -> {
                ctx.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
                if (arabic) "افتح إعدادات الواي فاي" else "Opening WiFi settings"
            }

            // bluetooth
            input.contains("toggle bluetooth") || input.contains("bluetooth") || input.contains("بلوتوث") -> {
                ctx.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
                if (arabic) "افتح إعدادات البلوتوث" else "Opening Bluetooth settings"
            }

            // simple time query
            input.contains("time") || input.contains("الساعة") -> {
                val now = java.time.LocalTime.now()
                val fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                if (arabic) "الوقت الآن ${now.format(fmt)}" else "It's ${now.format(fmt)}"
            }

            // memory: remember / تذكر
            input.startsWith("remember") || input.contains("تذكر") -> {
                // extract the phrase after the keyword
                val fact = when {
                    input.startsWith("remember") -> input.removePrefix("remember").trim()
                    input.contains("تذكر") -> {
                        val i = input.indexOf("تذكر")
                        input.substring(i + "تذكر".length).trim()
                    }
                    else -> ""
                }
                if (fact.isEmpty()) {
                    if (arabic) "ماذا تريد أن أتذكر؟" else "What should I remember?"
                } else {
                    // store a single last fact in a stable key
                    MemoryManager.put(ctx, "fact_last", fact)
                    if (arabic) "تم الحفظ: $fact" else "Saved: $fact"
                }
            }

            // recall last fact
            input.contains("what did i tell") || input.contains("what did i say") || input.contains("what did i tell you") || input.contains("ما الذي أخبرتني به") || input.contains("ماذا قلت") -> {
                val last = MemoryManager.get(ctx, "fact_last", if (arabic) "لا أتذكر" else "I don't remember")
                last
            }

            else -> if (arabic) "لم أفهم: '$text'" else "I didn't understand: '$text'"
        }
    }

    private fun openAppOrSettings(ctx: Context, name: String): String {
        if (name.isEmpty()) return "Open what?"
        // try package by label
        val pm = ctx.packageManager
        val apps = pm.getInstalledApplications(0)
        val match = apps.firstOrNull { (pm.getApplicationLabel(it).toString().lowercase()).contains(name) }
        return if (match != null) {
            val intent = pm.getLaunchIntentForPackage(match.packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ctx.startActivity(intent)
                "Opening ${pm.getApplicationLabel(match)}"
            } else {
                "Can't open ${pm.getApplicationLabel(match)}"
            }
        } else {
            // fallback: search Play Store
            val uri = Uri.parse("market://search?q=$name")
            ctx.startActivity(Intent(Intent.ACTION_VIEW, uri).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
            "Searching Play Store for $name"
        }
    }
}
