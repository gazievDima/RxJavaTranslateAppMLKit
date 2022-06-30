package com.gaziev.translate.app.fragments.photo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaziev.translate.app.fragments.dialogs.choose_from.ChooseFromLangDialogFragment
import com.gaziev.translate.app.fragments.dialogs.choose_to.ChooseToLangDialogFragment
import com.gaziev.translate.app.repository.FavoriteRepository
import com.gaziev.translate.core.MLKitTranslateText
import com.gaziev.translate.data.model.DictionaryEntity
import com.google.mlkit.nl.translate.TranslateLanguage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PhotoViewModel(
    private val repository: FavoriteRepository,
    private val mlKitTranslateText: MLKitTranslateText
    ) : ViewModel() {

    private var _translateFromLang: MutableLiveData<String> = MutableLiveData()
    val translateFromLang: LiveData<String> = _translateFromLang

    private var _translateToLang: MutableLiveData<String> = MutableLiveData()
    val translateToLang: LiveData<String> = _translateToLang

    private var compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun translate(recognizeString: String) {
         translateRecognizedText(
                recognizeString = recognizeString,
                langCodeForTranslate = ChooseFromLangDialogFragment.CHOOSE_FROM_LANG.code,
                chooseLangType = TypeTranslate.FromLanguage
            )

            translateRecognizedText(
                recognizeString = recognizeString,
                langCodeForTranslate = ChooseToLangDialogFragment.CHOOSE_TO_LANG.code,
                chooseLangType = TypeTranslate.ToLanguage
            )
    }

    private fun translateRecognizedText(
        recognizeString: String,
        initFromLang: String = TranslateLanguage.ENGLISH,
        langCodeForTranslate: String,
        chooseLangType: TypeTranslate
    ) {
        mlKitTranslateText.translateWithoutRecognizeLangs(
            recognizeString,
            initFromLang,
            langCodeForTranslate
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (chooseLangType) {
                    TypeTranslate.FromLanguage -> {
                        _translateFromLang.value = it
                    }
                    TypeTranslate.ToLanguage -> {
                        _translateToLang.value = it
                    }
                }
            }, {
                Log.e(TAG, "Cannot translated!")
            })
    }

    fun saveDictionary(item: DictionaryEntity) {
        compositeDisposable.add(
            repository.getAllDictionary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (miss(item, it)) {
                        Schedulers.io().createWorker().schedule {
                            repository.saveDictionary(item)
                        }
                    }
                }, {

                })
        )
    }

    private fun miss(item: DictionaryEntity, items: List<DictionaryEntity>): Boolean {
        for (el in items) {
            if (item.word == el.word) return false
        }
        return true
    }

}