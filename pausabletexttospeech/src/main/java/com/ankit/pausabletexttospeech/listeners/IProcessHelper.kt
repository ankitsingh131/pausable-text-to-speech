package com.ankit.pausabletexttospeech.listeners

internal interface IProcessHelper {

    fun onStarted(utteranceID:String?)

    fun onNext(utteranceID:String?)

    fun onError(utteranceID:String?)

    fun onCompleted(utteranceID:String?)
}