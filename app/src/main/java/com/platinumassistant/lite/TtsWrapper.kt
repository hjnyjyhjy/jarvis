package com.platinumassistant.lite

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

/**
 * Tiny Text-to-Speech wrapper. Lazy init to avoid memory use.
 * One method: speak(text). Keep it minimal and synchronous-friendly.
 */
class TtsWrapper(private val ctx: Context) {
    private var tts: TextToSpeech? = null

    private fun ensure() {
        if (tts != null) return
        tts = TextToSpeech(ctx) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
            }
        }
    }

    fun speak(text: String) {
        ensure()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "JL1")
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
    }
}
