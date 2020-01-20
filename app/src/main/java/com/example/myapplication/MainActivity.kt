package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ankit.pausabletexttospeech.PausableTextToSpeech
import com.ankit.pausabletexttospeech.listeners.IUtteranceHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), PausableTextToSpeech.OnInitListener, IUtteranceHelper,
    View.OnClickListener {

    private lateinit var pausableTextToSpeech: PausableTextToSpeech
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                Toast.makeText(this@MainActivity, msg.obj.toString(), Toast.LENGTH_SHORT)?.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        pausableTextToSpeech = PausableTextToSpeech(this, this)
        btnStart.setOnClickListener(this)
        btnResume.setOnClickListener(this)
        btnPause.setOnClickListener(this)
        btnStop.setOnClickListener(this)
    }

    override fun onInit() {
        pausableTextToSpeech.setOnUtteranceProgressListener(this)
        pausableTextToSpeech.addDelimiters(true, ".", "?", "\n", "!")
    }

    override fun onStart(utteranceId: String?) {
        val message = handler.obtainMessage(0)
        message.obj = "onStart: $utteranceId"
        handler.sendMessage(message)
    }

    override fun onResume(utteranceId: String?) {
        val message = handler.obtainMessage(0)
        message.obj = "onResume: $utteranceId"
        handler.sendMessage(message)
    }

    override fun onPause(utteranceId: String?) {
        val message = handler.obtainMessage(0)
        message.obj = "onPause: $utteranceId"
        handler.sendMessage(message)
    }

    override fun onStop(utteranceId: String?, interrupted: Boolean) {
        val message = handler.obtainMessage(0)
        message.obj = "onStop: $utteranceId"
        handler.sendMessage(message)
    }

    override fun onDone(utteranceId: String?) {
        val message = handler.obtainMessage(0)
        message.obj = "onDone: $utteranceId"
        handler.sendMessage(message)
    }

    override fun onError(utteranceId: String?) {
        val message = handler.obtainMessage(0)
        message.obj = "onError: $utteranceId"
        handler.sendMessage(message)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnStart -> {
                pausableTextToSpeech.start("Hey this is sample. Test | let value? Coding pausable tts!", PausableTextToSpeech.QUEUE_FLUSH, null, "dummyUtterance", true)
            }
            R.id.btnResume -> {
                pausableTextToSpeech.resume()
            }
            R.id.btnPause -> {
                pausableTextToSpeech.pause(false)
            }
            else -> {
                pausableTextToSpeech.stop()
            }
        }
    }
}
