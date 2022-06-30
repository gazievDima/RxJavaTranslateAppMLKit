package com.gaziev.translate.app.fragments.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gaziev.translate.app.App
import com.gaziev.translate.app.fragments.dialogs.choose_from.ChooseFromLangViewModel
import com.gaziev.translate.app.fragments.dialogs.choose_to.ChooseToLangViewModel
import com.gaziev.translate.app.fragments.dialogs.voice.VoiceDialogViewModel
import com.gaziev.translate.app.fragments.photo.PhotoViewModel
import com.gaziev.translate.app.fragments.stars.StarsViewModel
import com.gaziev.translate.app.fragments.text.TextViewModel
import com.gaziev.translate.app.repository.FavoriteRepository
import com.gaziev.translate.core.MLKitDefineLanguage
import com.gaziev.translate.core.MLKitTranslateText
import com.gaziev.translate.data.repository.FavoriteRepositoryImpl

class MainViewModelFactory : ViewModelProvider.Factory {

    private val starsDao = App.instance.getDataBase().getDao()
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