package com.myjoyy.composetutorial2.speechrecognition

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

//private const val LOG_TAG = "AudioRecordTest"
//private const val LOG_TAG = "SpeechRecognitionTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

//https://www.youtube.com/watch?v=Kr02N3p60Dg

interface SpeechToText {
    val text: StateFlow<String>
    fun start()
    fun stop()
    fun requestPermission(activity: Activity)
    fun hasPermission(context: Context): Boolean
}

class SpeechRecognition(private val context: Context) : SpeechToText {
    override val text = MutableStateFlow("")

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply{
        setRecognitionListener(object: RecognitionListener{
            override fun onReadyForSpeech(p0: Bundle?) = Unit
            override fun onBeginningOfSpeech() = Unit
            override fun onRmsChanged(p0: Float) = Unit
            override fun onBufferReceived(p0: ByteArray?) = Unit
            override fun onEndOfSpeech() = Unit
            override fun onResults(results: Bundle?) = Unit
            override fun onEvent(p0: Int, p1: Bundle?) = Unit

            override fun onPartialResults(results: Bundle?) {
                val partial = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.getOrNull(0)

                if (partial != null) {
                    text.value = partial
                }
            }

            override fun onError(error: Int) {
                val message = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio"
                    else -> "unknown"
                }
                Log.e("SpeechRecognizer", "Error: $message")
            }
        })
    }
    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
            2000
        )
    }
    override fun start(){
        if  (hasPermission(context)) {
            speechRecognizer.startListening(intent)
        }
    }
    override fun stop(){
        speechRecognizer.stopListening()
    }
    override fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    override fun requestPermission(activity: Activity){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            REQUEST_RECORD_AUDIO_PERMISSION
        )
    }
}