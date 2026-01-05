package com.platinumassistant.core.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale

class AndroidSpeechSynthesizer(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ready = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.getDefault()
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {}
                override fun onError(utteranceId: String?) {}
            })
            ready = true
            Timber.i("AndroidTTS initialized")
        } else {
            Timber.e("AndroidTTS init failed: %s", status)
        }
    }

    suspend fun initialize(): Boolean = withContext(Dispatchers.Main) {
        // already initialized in constructor; return ready state
        ready
    }

    fun speak(text: String, onComplete: (() -> Unit)? = null) {
        if (!ready) {
            Timber.w("TTS not ready")
            return
        }
        val params = hashMapOf<String, String>()
        params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "utterance_${System.currentTimeMillis()}"
        tts?.speak(text, TextToSpeech.QUEUE_ADD, params as java.util.HashMap<String, String>?, params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID])
    }

    fun stop() {
        tts?.stop()
    }

    fun isReady(): Boolean = ready

    fun shutdown() {
        try {
            tts?.shutdown()
        } catch (t: Throwable) {
            Timber.e(t)
        }
    }
}
