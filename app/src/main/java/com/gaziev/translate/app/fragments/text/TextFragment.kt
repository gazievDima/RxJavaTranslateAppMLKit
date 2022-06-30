package com.gaziev.translate.app.fragments.text

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gaziev.translate.R
import com.gaziev.translate.app.App
import com.gaziev.translate.app.fragments.common.BaseFragment
import com.gaziev.translate.app.fragments.common.MainViewModelFactory
import com.gaziev.translate.app.fragments.common.Result
import com.gaziev.translate.app.fragments.dialogs.choose_from.ChooseFromLangDialogFragment
import com.gaziev.translate.app.fragments.dialogs.choose_from.ChooseFromLangViewModel
import com.gaziev.translate.app.fragments.dialogs.choose_to.ChooseToLangDialogFragment
import com.gaziev.translate.app.fragments.dialogs.choose_to.ChooseToLangViewModel
import com.gaziev.translate.app.fragments.dialogs.voice.VoiceDialogViewModel
import com.gaziev.translate.app.fragments.stars.StarsFragment
import com.gaziev.translate.core.AdditionalEditText
import com.gaziev.translate.core.SimpleTextWatcher
import com.gaziev.translate.data.model.DictionaryEntity
import com.gaziev.translate.data.model.HistoryEntity
import com.gaziev.translate.databinding.FrTextBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class TextFragment : BaseFragment<FrTextBinding>() {

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FrTextBinding =
        FrTextBinding::inflate

    private val compositeDisposable = CompositeDisposable()
    private val additionalEditText by lazy { AdditionalEditText(requireActivity()) }
    private val viewModelChooseToLang by lazy {
        ViewModelProvider(requireActivity())[ChooseToLangViewModel::class.java]
    }
    private val viewModelChooseFromLang by lazy {
        ViewModelProvider(requireActivity())[ChooseFromLangViewModel::class.java]
    }
    private val viewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory())[TextViewModel::class.java]
    }
    private val viewModelVoiceDialog by lazy {
        ViewModelProvider(
            requireActivity(),
            MainViewModelFactory()
        )[VoiceDialogViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.containerChooseLang.tvFromLang.text =
            ChooseFromLangDialogFragment.CHOOSE_FROM_LANG.name
        binding.containerChooseLang.tvToLang.text = ChooseToLangDialogFragment.CHOOSE_TO_LANG.name
        observed()
        onClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        App.instance.getSharedPrefsSettings().saveLangs(
            langTo = ChooseToLangDialogFragment.CHOOSE_TO_LANG,
            langFrom = ChooseFromLangDialogFragment.CHOOSE_FROM_LANG
        )
    }

    private fun saveHistory(text: String) {

        if (StarsFragment.LAST_WORD_FROM_HISTORY.word != text) {
            viewModel.saveHistory(
                HistoryEntity(
                    word = binding.containerResultTranslate.outputText.text.toString(),
                    langCode = ChooseToLangDialogFragment.CHOOSE_TO_LANG.code
                )
            )

            StarsFragment.LAST_WORD_FROM_HISTORY = HistoryEntity(
                word = text,
                langCode = binding.containerChooseLang.tvToLang.text.toString()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelVoiceDialog.string.observe(viewLifecycleOwner) {
            binding.inputText.setText(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModelVoiceDialog.string.removeObservers(viewLifecycleOwner)
    }


    private fun observed() {
        val textWatcher = SimpleTextWatcher()
        binding.inputText.addTextChangedListener(textWatcher)
        textWatcher.behaviorSubject
            .debounce(700, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ textForTranslate ->

                binding.containerResultTranslate.outputText.text =
                    getString(R.string.translating_wait)
                binding.progressBar.visibility = View.VISIBLE

                viewModel.translateWithRecognized(textForTranslate) { code ->
                    binding.containerChooseLang.tvFromLang.text = code
                }
            }, {
                toast("Failed recognize text..")
            })


        viewModelChooseFromLang.selectedLanguage.observe(viewLifecycleOwner) {
            binding.apply {
                containerChooseLang.tvFromLang.text =
                    ChooseFromLangDialogFragment.CHOOSE_FROM_LANG.name
                containerResultTranslate.outputText.text = getString(R.string.translating_wait)
                progressBar.visibility = View.VISIBLE
            }

            viewModel.translateWithoutRecognized(binding.inputText.text.toString())
        }

        viewModelChooseToLang.selectedLanguage.observe(viewLifecycleOwner) {
            binding.apply {
                containerChooseLang.tvToLang.text = ChooseToLangDialogFragment.CHOOSE_TO_LANG.name
                containerResultTranslate.outputText.text = getString(R.string.translating_wait)
                progressBar.visibility = View.VISIBLE
            }
            viewModel.translateWithRecognized(binding.inputText.text.toString()) { code ->
                binding.containerChooseLang.tvFromLang.text = code
            }
        }

        viewModel.result.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE

            when (it) {
                is Result.Success -> {
                    val str = it.data as String
                    binding.containerResultTranslate.outputText.text = str
                    saveHistory(str)
                }
                else -> {
                    if (binding.inputText.text.isNotEmpty()) {
                        toast("Failed to translate..")
                    }

                    Log.e(TAG, "error: ${getString(R.string.we_cant_translated)}")
                }
            }
        }
    }

    private fun onClickListeners() {
        binding.apply {

            btnShareText.setOnClickListener {
                additionalEditText.shareText(binding.inputText)
            }

            containerResultTranslate.btnShareText.setOnClickListener {
                additionalEditText.shareText(containerResultTranslate.outputText)
            }

            btnToCopyClipboard.setOnClickListener {
                additionalEditText.copyText(inputText.text.toString())
            }

            containerResultTranslate.btnToCopyClipboard.setOnClickListener {
                additionalEditText.copyText(containerResultTranslate.outputText.text.toString())
            }

            btnOpenVoiceDialog.setOnClickListener {
                additionalEditText.voiceText(inputText.text.toString())
            }

            containerResultTranslate.btnOpenVoiceDialog.setOnClickListener {
                additionalEditText.voiceText(
                    containerResultTranslate.outputText.text.toString()
                )
            }

            containerChooseLang.tvFromLang.setOnClickListener {
                findNavController().navigate(R.id.chooseFromLangDialogFragment)
            }

            containerChooseLang.tvToLang.setOnClickListener {
                findNavController().navigate(R.id.chooseToLangDialogFragment)
            }

            btnSaveDictionary.setOnClickListener {
                viewModel.saveDictionary(
                    DictionaryEntity(
                        word = inputText.text.toString(),
                        langCode = ChooseFromLangDialogFragment.CHOOSE_FROM_LANG.code
                    )
                )
                toast(getString(R.string.saved_to_dict))
            }

            containerResultTranslate.btnSaveDictionary.setOnClickListener {
                viewModel.saveDictionary(
                    DictionaryEntity(
                        word = containerResultTranslate.outputText.text.toString(),
                        langCode = ChooseToLangDialogFragment.CHOOSE_TO_LANG.code
                    )
                )
                toast(getString(R.string.saved_to_dict))
            }
        }
    }

}