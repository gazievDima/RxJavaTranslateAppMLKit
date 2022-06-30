package com.gaziev.vcttranslate.app.fragments.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gaziev.vcttranslate.app.VCTtranslate
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_from.ChooseFromLangViewModel
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_to.ChooseToLangViewModel
import com.gaziev.vcttranslate.app.fragments.dialogs.voice.VoiceDialogViewModel
import com.gaziev.vcttranslate.app.fragments.photo.PhotoViewModel
import com.gaziev.vcttranslate.app.fragments.stars.StarsViewModel
import com.gaziev.vcttranslate.app.fragments.text.TextViewModel
import com.gaziev.vcttranslate.app.repository.FavoriteRepository
import com.gaziev.vcttranslate.core.MLKitDefineLanguage
import com.gaziev.vcttranslate.core.MLKitTranslateText
import com.gaziev.vcttranslate.data.repository.FavoriteRepositoryImpl

class MainViewModelFactory : ViewModelProvider.Factory {

    private val starsDao = VCTtranslate.instance.getDataBase().getDao()
    private val repository: FavoriteRepository = FavoriteRepositoryImpl(starsDao)
    private val mlKitDefineLanguage = MLKitDefineLanguage()
    private val mlKitTranslateText = MLKitTranslateText()


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StarsViewModel::class.java) ->
                StarsViewModel(repository) as T
            modelClass.isAssignableFrom(TextViewModel::class.java) ->
                TextViewModel(repository, mlKitTranslateText, mlKitDefineLanguage) as T
            modelClass.isAssignableFrom(ChooseFromLangViewModel::class.java) ->
                ChooseFromLangViewModel() as T
            modelClass.isAssignableFrom(ChooseToLangViewModel::class.java) ->
                ChooseToLangViewModel() as T
            modelClass.isAssignableFrom(VoiceDialogViewModel::class.java) ->
                VoiceDialogViewModel() as T
            modelClass.isAssignableFrom(PhotoViewModel::class.java) ->
                PhotoViewModel(repository, mlKitTranslateText) as T
            else -> modelClass.newInstance()
        }
    }

}