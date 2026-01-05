package com.platinumassistant.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.platinumassistant.core.ai.VoskManager
import timber.log.Timber

import com.platinumassistant.core.online.OnlineManager
import com.platinumassistant.core.prefs.PreferencesManager
import kotlinx.coroutines.flow.collectLatest

class ListeningViewModel : ViewModel() {
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening

    private val _lastTranscript = MutableStateFlow("")
    val lastTranscript: StateFlow<String> = _lastTranscript

    fun init(context: Context) {
        // Initialize Vosk for chosen language
        appContext = context.applicationContext
        viewModelScope.launch {
            val lang = PreferencesManager.getLanguage(context).firstOrNull() ?: "auto"
            val chosen = if (lang == "auto") "en" else lang
            VoskManager.initializeForLanguage(context, chosen)
        }
        // subscribe to online NLU url and online mode
        viewModelScope.launch {
            PreferencesManager.isOnlineModeEnabled(context).collectLatest { enabled ->
                _onlineMode = enabled
            }
        }
        viewModelScope.launch {
            PreferencesManager.getOnlineNluUrl(context).collectLatest { url ->
                _onlineNluUrl = url
            }
        }
    }

    fun startListening() {
        if (!VoskManager.isReady()) {
            Timber.w("Vosk not ready yet")
            return
        }
        _isListening.value = true
        VoskManager.startListening(onResult = { res ->
            viewModelScope.launch {
                _lastTranscript.value = res
                // If online mode and nlu url configured, call remote NLU
                if (_onlineMode && !_onlineNluUrl.isNullOrBlank()) {
                    val cleaned = extractTextFromVoskResult(res)
                    val nlu = OnlineManager.analyzeText(_onlineNluUrl!!, cleaned)
                    nlu?.let {
                        // Append remote NLU response to transcript for now
                        _lastTranscript.value = "${cleaned}\n→ intent: ${it.intent} (conf=${it.confidence})\n${it.response ?: ""}"
                    }
                } else {
                    // offline NLU: simple keyword NLU
                    try {
                        if (_offlineNlu == null) {
                            _offlineNlu = com.platinumassistant.core.nlu.KeywordNLUProcessor(context = appContext)
                            _offlineNlu?.initialize("")
                            // set language
                            val lang = PreferencesManager.getLanguage(appContext).firstOrNull() ?: "en"
                            _offlineNlu?.setLanguage(if (lang == "auto") "en" else lang)
                        }
                        val cleaned = extractTextFromVoskResult(res)
                        val out = _offlineNlu?.process(cleaned)
                        out?.let { r ->
                            viewModelScope.launch {
                                _lastTranscript.value = "${cleaned}\n→ intent: ${r.intent}"
                            }
                        }
                    } catch (t: Throwable) {
                        Timber.e(t, "Offline NLU error")
                    }
                }
            }
        }, onError = { t ->
            Timber.e(t, "STT error")
            _isListening.value = false
        })
    }

    private var _offlineNlu: com.platinumassistant.core.nlu.KeywordNLUProcessor? = null
    private lateinit var appContext: Context

    private fun extractTextFromVoskResult(resultJson: String): String {
        // Vosk returns JSON like {"text":"..."}. Do a simple extraction to keep dependency light.
        val regex = "\"text\"\s*:\s*\"(.*?)\"".toRegex()
        val match = regex.find(resultJson)
        return match?.groups?.get(1)?.value ?: resultJson
    }

    private var _onlineMode: Boolean = false
    private var _onlineNluUrl: String? = null

    fun stopListening() {
        VoskManager.stopListening()
        _isListening.value = false
    }
}
