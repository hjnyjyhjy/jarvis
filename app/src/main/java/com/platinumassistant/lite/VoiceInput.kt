package com.platinumassistant.lite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Minimal, lazy wrapper around Android SpeechRecognizer.
 * - No background services
 * - Creates recognizer on demand and releases it
 */
class VoiceInput(private val ctx: Context? = null) {
    private var recognizer: SpeechRecognizer? = null
    private var listener: ((String) -> Unit)? = null

    private fun ensure(ctx: Context): SpeechRecognizer {
        if (recognizer == null) recognizer = SpeechRecognizer.createSpeechRecognizer(ctx)
        return recognizer!!
    }

    fun startListening(callback: (String) -> Unit) {
        listener = callback
        val c = ctx ?: throw IllegalStateException("Context required for SpeechRecognizer")
        val sr = ensure(c)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        sr.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onError(error: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    listener?.invoke(" recognition error: $error")
                }
                release()
            }

            override fun onResults(results: Bundle?) {
                val list = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = list?.firstOrNull() ?: ""
                CoroutineScope(Dispatchers.Main).launch {
                    listener?.invoke(text)
                }
                release()
            }
        })

        sr.startListening(intent)
    }

    fun stopListening() {
        recognizer?.cancel()
        release()
    }

    private fun release() {
        try {
            recognizer?.destroy()
        } catch (_: Exception) {
        }
        recognizer = null
    }
}
