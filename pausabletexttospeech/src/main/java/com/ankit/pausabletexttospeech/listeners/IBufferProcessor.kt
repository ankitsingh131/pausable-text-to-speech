package com.ankit.pausabletexttospeech.listeners

interface IBufferProcessor {

    fun process(speech: String, utteranceID: String?)

    fun clear()
}