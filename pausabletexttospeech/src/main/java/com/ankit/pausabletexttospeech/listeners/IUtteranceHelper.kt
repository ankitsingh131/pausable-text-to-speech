package com.ankit.pausabletexttospeech.listeners

interface IUtteranceHelper {

    fun onStart(utteranceId: String?)

    fun onResume(utteranceId: String?)

    fun onPause(utteranceId: String?)

    fun onStop(utteranceId: String?, interrupted: Boolean)

    fun onDone(utteranceId: String?)

    fun onError(utteranceId: String?)

}