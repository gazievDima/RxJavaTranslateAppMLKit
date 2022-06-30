package com.gaziev.translate.app.fragments.dialogs.choose_from

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaziev.translate.core.Language

class ChooseFromLangViewModel : ViewModel() {

    private var _selectedLanguage: MutableLiveData<Language> = MutableLiveData(Language("en", "English"))
    val selectedLanguage: LiveData<Language> = _selectedLanguage

    fun chooseLanguage(language: Language) {
        ChooseFromLangDialogFragment.CHOOSE_FROM_LANG = language
        _selectedLanguage.value = language
    }
}