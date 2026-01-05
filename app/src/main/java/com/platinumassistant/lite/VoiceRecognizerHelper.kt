package com.platinumassistant.lite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

// Minimal SpeechRecognizer helper. One feature per file, tiny and lazy.
object VoiceRecognizerHelper {
    private var recognizer: SpeechRecognizer? = null
    private var listening = false

    fun startListening(ctx: Context, callback: (String) -> Unit) {
        if (listening) return
        try {
            if (!SpeechRecognizer.isRecognitionAvailable(ctx)) {
                callback("Speech recognition unavailable")
                return
            }

            recognizer = SpeechRecognizer.createSpeechRecognizer(ctx)
            recognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
                override fun onPartialResults(partialResults: Bundle?) {}

                override fun onError(error: Int) {
                    callback("error:$error")
                    stopListening()
                }

                override fun onResults(results: Bundle?) {
                    val list = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    callback(list?.firstOrNull() ?: "")
                    stopListening()
                }
            })

            val it = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }
            recognizer?.startListening(it)
            listening = true
        } catch (e: Exception) {
            callback("exception:${e.message}")
            stopListening()
        }
    }

    fun stopListening() {
        try {
            recognizer?.cancel()
            recognizer?.destroy()
        } catch (_: Exception) {
        }
        recognizer = null
        listening = false
    }

    fun isListening(): Boolean = listening
}
