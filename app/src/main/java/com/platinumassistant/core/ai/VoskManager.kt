package com.platinumassistant.core.ai

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Simple manager to hold and initialize a single VoskSpeechRecognizer instance.
 */
object VoskManager {
    private var recognizer: VoskSpeechRecognizer? = null

    fun initialize(context: Context, modelDirName: String = "vosk") {
        val modelPath = context.filesDir.absolutePath + "/models/$modelDirName"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val r = VoskSpeechRecognizer()
                r.initialize(modelPath)
                recognizer = r
                Timber.i("VoskManager initialized with model at %s", modelPath)
            } catch (t: Throwable) {
                Timber.e(t, "Failed to init Vosk recognizer")
            }
        }
    }

    fun initializeForLanguage(context: Context, language: String) {
        // map language to model dir names; convention: "vosk-en-us" or "vosk-ar"
        val dirName = when (language) {
            "ar" -> "vosk-ar"
            "en" -> "vosk-en-us"
            else -> "vosk-en-us"
        }
        initialize(context, dirName)
    }

    fun startListening(onResult: (String) -> Unit, onError: (Throwable) -> Unit) {
        recognizer?.startListening(onResult, onError) ?: onError(IllegalStateException("Vosk not initialized"))
    }

    fun stopListening() {
        recognizer?.stopListening()
    }

    fun isReady(): Boolean = recognizer?.isReady() == true
}
