package com.gaziev.vcttranslate.app.fragments.dialogs.voice

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gaziev.vcttranslate.R
import com.gaziev.vcttranslate.app.fragments.common.MainViewModelFactory
import com.gaziev.vcttranslate.databinding.DialogFrVoiceBinding
import java.util.*


class VoiceDialogFragment : DialogFragment() {
    private var binding: DialogFrVoiceBinding? = null
    private val viewModel: VoiceDialogViewModel by lazy {
        ViewModelProvider(requireActivity(), MainViewModelFactory())[VoiceDialogViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_fr_voice, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogFrVoiceBinding.bind(view)

        onClickListeners()

    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams = dialog?.window?.attributes!!
        params.width = 900
        params.height = 1200
        dialog?.window?.attributes = params as WindowManager.LayoutParams

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )
            if(result != null) {
                for (el in result) {
                    viewModel.saveString(el)
                    binding?.textView?.text = el
                }
                this.dismiss()
            }
        }
    }


    private fun startVoice() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

        try {
            startActivityForResult(intent, 1234)
        } catch (e: Exception) {
            Toast
                .makeText(
                    requireContext(), " " + e.message,
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    private fun onClickListeners() {
        binding?.btnCloseDialog?.setOnClickListener {
            this.dismiss()
        }

        binding?.voiceImage?.setOnClickListener {
            startVoice()
        }
    }

}