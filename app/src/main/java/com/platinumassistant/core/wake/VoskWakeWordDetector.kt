package com.platinumassistant.core.wake

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.vosk.Model
import org.vosk.Recognizer
import timber.log.Timber

/**
 * A simple wake-word detector that uses Vosk keyword spotting grammar.
 * It uses a small model and a limited grammar like ["jarvis","hey jarvis"].
 * When a keyword is detected it calls `onDetected`.
 */
class VoskWakeWordDetector(private val sampleRate: Int = 16000, private val keywords: List<String> = listOf("jarvis")) : WakeWordDetector {
    private var model: Model? = null
    private var recognizer: Recognizer? = null
    private var audioRecord: AudioRecord? = null
    @Volatile private var listening = false

    override suspend fun initialize(modelPath: String?) = withContext(Dispatchers.IO) {
        if (modelPath == null) throw IllegalArgumentException("modelPath required")
        model = Model(modelPath)
        Timber.i("Vosk wake model initialized at %s", modelPath)
    }

    override fun startListening(onDetected: () -> Unit, onError: (Throwable) -> Unit) {
        if (model == null) {
            onError(IllegalStateException("Vosk wake model not initialized"))
            return
        }

        if (listening) return
        listening = true

        val grammar = keywords.joinToString(prefix = "[\"", separator = "\",\"", postfix = "\"]")
        recognizer = Recognizer(model, sampleRate.toFloat(), grammar)

        val minBuf = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        val buffer = ShortArray(minBuf / 2)
        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBuf)
        audioRecord?.startRecording()

        Thread {
            try {
                while (listening) {
                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        if (recognizer?.acceptWaveForm(buffer, read) == true) {
                            val res = recognizer?.result
                            // Vosk returns JSON result — check for text match
                            res?.let {
                                if (it.contains("\"text\"")) {
                                    // naive check; higher level should parse JSON for text
                                    if (keywords.any { kw -> it.contains(kw, ignoreCase = true) }) {
                                        onDetected()
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }.start()
    }

    override fun stopListening() {
        listening = false
        try {
            audioRecord?.stop()
            audioRecord?.release()
        } catch (t: Throwable) {
            Timber.e(t)
        }
        recognizer = null
        audioRecord = null
    }

    override fun isReady(): Boolean = model != null
}
