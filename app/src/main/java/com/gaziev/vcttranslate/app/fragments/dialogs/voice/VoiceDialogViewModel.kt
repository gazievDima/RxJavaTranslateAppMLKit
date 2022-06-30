package com.gaziev.vcttranslate.app.fragments.dialogs.voice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VoiceDialogViewModel : ViewModel() {

    private var _string: MutableLiveData<String> = MutableLiveData()
    val string: LiveData<String> = _string

    fun saveString(str: String) {
        _string.value = str
    }

}