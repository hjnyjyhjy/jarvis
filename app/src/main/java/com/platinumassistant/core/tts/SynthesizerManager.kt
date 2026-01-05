package com.platinumassistant.core.tts

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

object SynthesizerManager {
    private var androidTts: AndroidSpeechSynthesizer? = null

    fun initialize(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                androidTts = AndroidSpeechSynthesizer(context.applicationContext)
                androidTts?.initialize()
            } catch (t: Throwable) {
                Timber.e(t, "Failed to initialize Android TTS")
            }
        }
    }

    fun speak(text: String) {
        androidTts?.speak(text)
    }

    fun stop() {
        androidTts?.stop()
    }

    fun shutdown() {
        androidTts?.shutdown()
    }
}
