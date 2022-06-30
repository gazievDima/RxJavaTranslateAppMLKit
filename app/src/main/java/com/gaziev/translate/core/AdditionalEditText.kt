package com.gaziev.translate.core

import android.app.Activity
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.widget.TextView
import android.widget.Toast
import com.gaziev.translate.utils.copyToClipboard
import java.util.*

class AdditionalEditText(
    private val activity: Activity,

    ) {

    private var textToSpeech: TextToSpeech? = null

    init {
        textToSpeech = TextToSpeech(
            activity
        ) { i ->
            if (i != TextToSpeech.ERROR) {
                textToSpeech?.language = Locale.ENGLISH
            }
        }
    }

    fun shareText(view: TextView) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, view.text.toString())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        activity.startActivity(shareIntent)
    }

    fun copyText(str: String) {
        activity.copyToClipboard(str, str)

        Toast.makeText(activity, "Copied: $str", Toast.LENGTH_SHORT).show()
    }

    fun voiceText(str: String) {
        textToSpeech?.speak(
            str,
            TextToSpeech.QUEUE_FLUSH,
            null
        )
    }
}