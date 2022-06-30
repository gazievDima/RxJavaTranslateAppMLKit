package com.gaziev.vcttranslate.app.fragments.dialogs.choose_to

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaziev.vcttranslate.app.VCTtranslate
import com.gaziev.vcttranslate.core.Language

class ChooseToLangViewModel : ViewModel() {

    private var _selectedLanguage: MutableLiveData<Language> = MutableLiveData(Language("en", "English"))
    val selectedLanguage: LiveData<Language> = _selectedLanguage

    fun chooseLanguage(language: Language) {
        ChooseToLangDialogFragment.CHOOSE_TO_LANG = language
        _selectedLanguage.value = language
    }
}