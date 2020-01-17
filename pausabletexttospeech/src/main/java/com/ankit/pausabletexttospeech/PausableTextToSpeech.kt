package com.ankit.pausabletexttospeech

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import com.ankit.pausabletexttospeech.annotations.State
import com.ankit.pausabletexttospeech.annotations.TtsState
import com.ankit.pausabletexttospeech.buffer.BufferProcessor
import com.ankit.pausabletexttospeech.listeners.IUtteranceHelper
import java.util.*

class PausableTextToSpeech(
    private val context: Context,
    private val onInitListener: OnInitListener
) {

    @TtsState
    var state: Int = State.STOPPED
    private val textToSpeech: TextToSpeech
    private var canSpeak: Boolean = false
    private val bufferProcessor: BufferProcessor
    private var utteranceHelper: IUtteranceHelper? = null
    private var ttsBundle: Bundle? = null
    private var queueMode: Int = TextToSpeech.QUEUE_FLUSH

    interface OnInitListener {
        fun onInit()
    }

    init {
        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener {
            onInit(it)
        })
        bufferProcessor = BufferProcessor(this@PausableTextToSpeech)
    }

    fun setOnUtteranceProgressListener(utteranceHelper: IUtteranceHelper) {
        this.utteranceHelper = utteranceHelper
    }

    private fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            canSpeak = true
            textToSpeech.language = Locale.getDefault()
            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onDone(utteranceId: String?) {
                    val next = bufferProcessor.next()
                    val let: Int? = next?.let {
                        state = State.PLAYING
                        println("Speaking utterance: ${next.speech}")
                        return@let textToSpeech.speak(
                            next.speech,
                            queueMode,
                            ttsBundle,
                            next.utteranceID
                        )
                    }
                    if (let == null) {
                        println("let is null.")
                        utteranceHelper?.onDone(utteranceId)
                    }
                    println("Value of let: $let")
                }

                override fun onError(utteranceId: String?) {
                    utteranceHelper?.onError(bufferProcessor.utteranceID)
                }

                override fun onStart(utteranceId: String?) {

                }
            })
            onInitListener.onInit()
        } else {
            val mainLooper = Looper.getMainLooper()
            if (mainLooper?.thread == Thread.currentThread()) {
                Toast.makeText(context, "Error in initializing Text-To-Speech", Toast.LENGTH_SHORT)
                    ?.show()
            }
        }
    }

    fun start(
        speech: String,
        queueMode: Int,
        ttsBundle: Bundle?,
        utteranceID: String?,
        clearBuffer: Boolean
    ): Int {
        if (clearBuffer || queueMode == TextToSpeech.QUEUE_FLUSH) {
            bufferProcessor.clear()
        }
        if (canSpeak) {
            this@PausableTextToSpeech.queueMode = queueMode
            this@PausableTextToSpeech.ttsBundle = ttsBundle
            bufferProcessor.process(speech, utteranceID)
            val next = bufferProcessor.next()
            next?.let {
                state = State.PLAYING
                utteranceHelper?.onStart(next.utteranceID)
                println("Speaking utterance: ${next.speech}")
                return textToSpeech.speak(next.speech, queueMode, ttsBundle, next.utteranceID)
            }
        }
        return TextToSpeech.ERROR
    }

    fun resume() {
        val current = bufferProcessor.current()
        current?.let {
            state = State.PLAYING
            utteranceHelper?.onStart(current.utteranceID)
            println("Speaking utterance: ${current.speech}")
            textToSpeech.speak(current.speech, queueMode, ttsBundle, current.utteranceID)
        }
    }

    fun pause(clearBuffer: Boolean) {
        state = if (clearBuffer) {
            utteranceHelper?.onStop(bufferProcessor.utteranceID, true)
            bufferProcessor.clear()
            State.STOPPED
        } else {
            utteranceHelper?.onPause(bufferProcessor.utteranceID)
            State.PAUSED
        }
        textToSpeech.stop()
    }

    fun stop() {
        pause(true)
    }

    companion object {
        const val QUEUE_ADD = TextToSpeech.QUEUE_ADD
        const val QUEUE_FLUSH = TextToSpeech.QUEUE_FLUSH
    }
}