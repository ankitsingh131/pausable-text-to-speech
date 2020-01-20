package com.ankit.pausabletexttospeech.buffer

import com.ankit.pausabletexttospeech.PausableTextToSpeech
import com.ankit.pausabletexttospeech.listeners.IBufferProcessor
import com.ankit.pausabletexttospeech.models.Speech
import java.util.*
import kotlin.collections.ArrayList

class BufferProcessor(pausableTextToSpeech: PausableTextToSpeech) : IBufferProcessor {

    private var currentDelimiter = DEFAULT_DELIMITER
    private val bufferList: MutableList<Speech> = ArrayList()
    private var currentSpeech: Speech? = null
    private var currentSpeechIndex: Int = -1
    internal var utteranceID: String? = null

    companion object {

        private const val WORD = " "
        private const val LINE = ".?!\n"
        private const val PARAGRAPH = "\n"
        private const val DEFAULT_DELIMITER: String = LINE
        internal var INVISIBLE_CHARACTER: String = "\u2063"
    }

    internal fun addDelimiters(addInPreviousDelimiters: Boolean = false, vararg delimiters: String) {
        if (!addInPreviousDelimiters) {
            currentDelimiter = ""
        }
        currentDelimiter = delimiters.distinct().filter { !currentDelimiter.contains(it) }
            .joinToString(separator = "", prefix = currentDelimiter, truncated = "")
    }

    internal fun useDefaultDelimiter() {
        currentDelimiter = DEFAULT_DELIMITER
    }

    override fun process(
        speech: String,
        utteranceID: String?
    ) {
        this.utteranceID = utteranceID
        val speechList: MutableList<Speech> = ArrayList()
        processSpeechWithBreakStrategy(
            speech = speech,
            speechList = speechList,
            breakStrategy = currentDelimiter,
            utteranceID = utteranceID
        )
        if (speechList.size > 0) {
            bufferList.addAll(0, speechList)
        }
    }

    private fun processSpeechWithBreakStrategy(
        speech: String,
        speechList: MutableList<Speech>,
        breakStrategy: String = LINE,
        utteranceID: String?
    ) {
        if (breakStrategy.isEmpty()) {
            speechList.add(Speech(speech, utteranceID))
        } else {
            val stringTokenizer = StringTokenizer(speech, breakStrategy)
            var isFirst = true
            while (stringTokenizer.hasMoreTokens()) {
                speechList.add(
                    Speech(
                        stringTokenizer.nextToken(),
                        if (isFirst || !stringTokenizer.hasMoreTokens()) utteranceID else if (utteranceID.isNullOrEmpty()) "" else "default${INVISIBLE_CHARACTER}$utteranceID"
                    )
                )
                isFirst = false
            }
        }
    }

    fun next(): Speech? {
        currentSpeechIndex++
        if (currentSpeechIndex < bufferList.size) {
            currentSpeech = bufferList[currentSpeechIndex]
            return currentSpeech
        }
        return null
    }

    fun current(): Speech? {
        return currentSpeech
    }

    fun previous(): Speech? {
        currentSpeechIndex--
        if (currentSpeechIndex > -1) {
            currentSpeech = bufferList[currentSpeechIndex]
            return currentSpeech
        }
        return null
    }

    override fun clear() {
        resetCurrent()
        bufferList.clear()
    }

    private fun resetCurrent() {
        currentSpeech = null
        currentSpeechIndex = -1
    }
}