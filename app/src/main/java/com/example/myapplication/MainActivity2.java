package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ankit.pausabletexttospeech.PausableTextToSpeech;
import com.ankit.pausabletexttospeech.listeners.IUtteranceHelper;

public class MainActivity2 extends AppCompatActivity implements PausableTextToSpeech.OnInitListener, IUtteranceHelper {

    private PausableTextToSpeech pausableTextToSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pausableTextToSpeech = new PausableTextToSpeech(this, this);
    }

    @Override
    public void onInit() {
        pausableTextToSpeech.setOnUtteranceProgressListener(this);
    }

    @Override
    public void onStart(@Nullable String utteranceId) {
        // Called when utterance started.
    }

    @Override
    public void onResume(@Nullable String utteranceId) {
        // Called when utterance resumed.
    }

    @Override
    public void onPause(@Nullable String utteranceId) {
        // Called when utterance paused.
    }

    @Override
    public void onStop(@Nullable String utteranceId, boolean interrupted) {
        // Called when utterance interrupted.
    }

    @Override
    public void onDone(@Nullable String utteranceId) {
        // Called when utterance successfully completed.
    }

    @Override
    public void onError(@Nullable String utteranceId) {
        // Called when Text-To-Speech unable speak utterance.
    }
}
