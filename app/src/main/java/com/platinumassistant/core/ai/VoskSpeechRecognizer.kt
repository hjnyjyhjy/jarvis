package com.platinumassistant.core.ai

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.vosk.Model
import org.vosk.Recognizer
import timber.log.Timber

/**
 * Vosk-based offline SpeechRecognizer implementation.
 * - Requires a Vosk model extracted locally (see `models_manifest.json`).
 */
class VoskSpeechRecognizer(private val sampleRate: Int = 16000) : SpeechRecognizer {
    private var model: Model? = null
    private var recognizer: Recognizer? = null
    private var audioRecord: AudioRecord? = null
    @Volatile private var listening = false

    override suspend fun initialize(modelPath: String) = withContext(Dispatchers.IO) {
        model = Model(modelPath)
        // recognizer will be created when listening starts (with a chosen grammar or null)
        Timber.i("Vosk model initialized at %s", modelPath)
    }

    override fun startListening(onResult: (String) -> Unit, onError: (Throwable) -> Unit) {
        if (model == null) {
            onError(IllegalStateException("Vosk model is not initialized"))
            return
        }

        if (listening) return
        listening = true

        val minBuf = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
        val buffer = ShortArray(minBuf / 2)
        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBuf)
        recognizer = Recognizer(model, sampleRate.toFloat())

        audioRecord?.startRecording()

        Thread {
            try {
                while (listening) {
                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        if (recognizer?.acceptWaveForm(buffer, read) == true) {
                            val res = recognizer?.result
                            res?.let { onResult(it) }
                        } else {
                            val partial = recognizer?.partialResult
                            // Optionally expose partial results
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
