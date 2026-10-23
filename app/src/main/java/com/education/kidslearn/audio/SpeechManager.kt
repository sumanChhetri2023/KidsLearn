package com.education.kidslearn.audio

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

/** Owns TTS and reports spelling progress from actual utterance callbacks. */
class SpeechManager(context: Context) {
    private var textToSpeech: TextToSpeech? = null
    private var speechReady = false
    private var pendingSpeechAction: (() -> Unit)? = null
    private var spellingProgressCallback: ((Int) -> Unit)? = null
    private var spellingSession = 0L
    private val speechLocale = Locale.US
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        textToSpeech = TextToSpeech(context.applicationContext) { status ->
            val tts = textToSpeech
            if (status == TextToSpeech.SUCCESS && tts != null) {
                val result = tts.setLanguage(speechLocale)
                speechReady = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
                if (speechReady) {
                    tts.setSpeechRate(0.85f)
                    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            val parts = utteranceId?.split(":") ?: return
                            if (parts.size == 3 && parts[0] == "spell" &&
                                parts[1].toLongOrNull() == spellingSession
                            ) {
                                val index = parts[2].toIntOrNull() ?: return
                                mainHandler.post { spellingProgressCallback?.invoke(index) }
                            }
                        }

                        override fun onDone(utteranceId: String?) {
                            val parts = utteranceId?.split(":") ?: return
                            if (parts.size >= 2 && parts[0] == "spellDone" &&
                                parts[1].toLongOrNull() == spellingSession
                            ) {
                                mainHandler.post { spellingProgressCallback?.invoke(-1) }
                            }
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            mainHandler.post { spellingProgressCallback?.invoke(-1) }
                        }
                    })
                    val action = pendingSpeechAction
                    pendingSpeechAction = null
                    action?.invoke()
                } else pendingSpeechAction = null
            } else pendingSpeechAction = null
        }
    }

    fun setSpellingProgressCallback(callback: ((Int) -> Unit)?) {
        spellingProgressCallback = callback
    }

    fun speakWord(word: String) {
        if (word.isBlank()) return
        val tts = textToSpeech
        if (!speechReady || tts == null) {
            pendingSpeechAction = { speakWord(word) }
            return
        }
        pendingSpeechAction = null
        spellingSession += 1
        spellingProgressCallback?.invoke(-1)
        tts.stop()
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, "word_${System.nanoTime()}")
    }

    fun spellWord(word: String) {
        if (word.isBlank()) return
        val tts = textToSpeech
        if (!speechReady || tts == null) {
            pendingSpeechAction = { spellWord(word) }
            return
        }
        pendingSpeechAction = null
        tts.stop()
        spellingSession += 1
        val session = spellingSession
        spellingProgressCallback?.invoke(-1)
        if (word.all { it.isDigit() }) {
            tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, "number_${System.nanoTime()}")
            return
        }
        val letters = word.uppercase(Locale.US).filter { it in 'A'..'Z' }
        if (letters.isEmpty()) {
            speakWord(word)
            return
        }
        letters.forEachIndexed { index, letter ->
            tts.speak(
                letter.toString(),
                if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD,
                null,
                "spell:$session:$index"
            )
            if (index < letters.lastIndex) {
                tts.playSilentUtterance(300L, TextToSpeech.QUEUE_ADD, "pause_$index")
            }
        }
        tts.speak(word, TextToSpeech.QUEUE_ADD, null, "spellDone:$session")
    }

    fun stopSpeech() {
        pendingSpeechAction = null
        spellingSession += 1
        spellingProgressCallback?.invoke(-1)
        textToSpeech?.stop()
    }

    fun shutdown() {
        textToSpeech?.shutdown()
        textToSpeech = null
        spellingProgressCallback = null
    }
}
