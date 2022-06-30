package com.gaziev.vcttranslate.app.fragments.photo

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gaziev.vcttranslate.R
import com.gaziev.vcttranslate.app.VCTtranslate
import com.gaziev.vcttranslate.app.fragments.common.BaseFragment
import com.gaziev.vcttranslate.app.fragments.common.MainViewModelFactory
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_from.ChooseFromLangDialogFragment
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_from.ChooseFromLangViewModel
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_to.ChooseToLangDialogFragment
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_to.ChooseToLangViewModel
import com.gaziev.vcttranslate.core.AdditionalEditText
import com.gaziev.vcttranslate.core.CameraPreview
import com.gaziev.vcttranslate.data.model.DictionaryEntity
import com.gaziev.vcttranslate.databinding.FrPhotoBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PhotoFragment : BaseFragment<FrPhotoBinding>() {

    companion object {
        const val REQUEST_CODE = 1234
    }

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FrPhotoBinding =
        FrPhotoBinding::inflate

    private val compositeDisposable = CompositeDisposable()
    private val additionalEditText by lazy { AdditionalEditText(requireActivity()) }
    private val viewModel: PhotoViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory())[PhotoViewModel::class.java]
    }
    private val viewModelChooseToLang by lazy {
        ViewModelProvider(requireActivity())[ChooseToLangViewModel::class.java]
    }
    private val viewModelChooseFromLang by lazy {
        ViewModelProvider(requireActivity())[ChooseFromLangViewModel::class.java]
    }
    private val cameraPreview: CameraPreview by lazy {
        CameraPreview(this, requireActivity(), binding.previewView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.containerChooseLang.tvFromLang.text =
            ChooseFromLangDialogFragment.CHOOSE_FROM_LANG.name
        binding.containerChooseLang.tvToLang.text = ChooseToLangDialogFragment.CHOOSE_TO_LANG.name
        observed()
        requestCamera()
        clickListeners()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.btnTakePhoto.isEnabled = true
            cameraPreview.cameraOn()
        } else {
            binding.btnTakePhoto.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        VCTtranslate.instance.getSharedPrefsSettings().saveLangs(
            langTo = ChooseToLangDialogFragment.CHOOSE_TO_LANG,
            langFrom = ChooseFromLangDialogFragment.CHOOSE_FROM_LANG
        )
    }

    private fun observed() {
        viewModel.translateFromLang.observe(viewLifecycleOwner) {
            binding.containerResultTranslate.outputTextFromLang.text = it
        }

        viewModel.translateToLang.observe(viewLifecycleOwner) {
            binding.containerResultTranslate.outputTextToLang.text = it
        }

        viewModelChooseFromLang.selectedLanguage.observe(viewLifecycleOwner) {
            binding.apply {
                containerChooseLang.tvFromLang.text =
                    ChooseFromLangDialogFragment.CHOOSE_FROM_LANG.name
            }
        }

        viewModelChooseToLang.selectedLanguage.observe(viewLifecycleOwner) {
            binding.apply {
                containerChooseLang.tvToLang.text = ChooseToLangDialogFragment.CHOOSE_TO_LANG.name
                containerResultTranslate.outputTextToLang.text =
                    getString(R.string.recognized)
            }
        }
    }

    private fun requestCamera() {
        requestPermissions(arrayOf("android.permission.CAMERA"), REQUEST_CODE)
    }

    private fun showStateRecognize() {
        binding.progressbar.visibility = View.VISIBLE
        binding.identification.visibility = View.VISIBLE
    }

    private fun goneStateRecognize() {
        binding.progressbar.visibility = View.GONE
        binding.identification.visibility = View.GONE
    }

    private fun clickListeners() {

        binding.containerChooseLang.tvFromLang.setOnClickListener {
            findNavController().navigate(R.id.chooseFromLangDialogFragment)
        }

        binding.containerChooseLang.tvToLang.setOnClickListener {
            findNavController().navigate(R.id.chooseToLangDialogFragment)
        }

        binding.btnTakePhoto.setOnClickListener {
            showStateRecognize()

            compositeDisposable.add(
                cameraPreview.cameraTakePhoto()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ recognizeString ->
                        viewModel.translate(recognizeString)
                        goneStateRecognize()
                    }, {
                        goneStateRecognize()
                        toast("Cannot recognize object!")
                    })
            )
        }

        binding.containerResultTranslate.apply {

            btnOpenVoiceDialog.setOnClickListener {
                additionalEditText.voiceText(
                    outputTextToLang.text.toString()
                )
            }

            btnShareText.setOnClickListener {
                additionalEditText.shareText(outputTextToLang)
            }

            btnToCopyClipboard.setOnClickListener {
                additionalEditText.copyText(outputTextToLang.text.toString())
            }

            btnSaveDictionary.setOnClickListener {
                viewModel.saveDictionary(
                    DictionaryEntity(
                        word = outputTextToLang.text.toString(),
                        langCode = ChooseToLangDialogFragment.CHOOSE_TO_LANG.code
                    )
                )
                toast(getString(R.string.saved_to_dict))
            }
        }
    }
}