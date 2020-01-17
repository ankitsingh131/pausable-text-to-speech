package com.ankit.pausabletexttospeech.annotations

import androidx.annotation.IntDef

object State {

    const val PAUSED = 1
    const val PLAYING = 2
    const val STOPPED = 3

    fun ttsStateToString(@TtsState state: Int): String {
        return when (state) {
            PAUSED -> "PAUSED"
            PLAYING -> "PLAYING"
            else -> "STOPPED"
        }
    }
}

@IntDef(State.PAUSED, State.PLAYING, State.STOPPED)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class TtsState