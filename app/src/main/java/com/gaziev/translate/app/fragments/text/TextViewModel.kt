package com.gaziev.translate.app.fragments.text

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaziev.translate.app.fragments.common.Result
import com.gaziev.translate.app.fragments.dialogs.choose_from.ChooseFromLangDialogFragment
import com.gaziev.translate.app.fragments.dialogs.choose_to.ChooseToLangDialogFragment
import com.gaziev.translate.app.repository.FavoriteRepository
import com.gaziev.translate.core.MLKitDefineLanguage
import com.gaziev.translate.core.MLKitTranslateText
import com.gaziev.translate.data.model.DictionaryEntity
import com.gaziev.translate.data.model.HistoryEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TextViewModel(
    private val repository: FavoriteRepository,
    private val mlKitTranslateText: MLKitTranslateText,
    private val mlKitDefineLanguage: MLKitDefineLanguage
) : ViewModel() {

    private var _result: MutableLiveData<Result> = MutableLiveData()
    val result: LiveData<Result> = _result

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun translateWithRecognized(textForTranslate: String, fromLangCode: (code: String) -> Unit) {
        compositeDisposable.add(
            mlKitTranslateText.translateWithRecognizeLangs(
                text = textForTranslate,
                toLang = ChooseToLangDialogFragment.CHOOSE_TO_LANG.code,
                mlKitDefineLanguage,
                reserveFromLang = ChooseFromLangDialogFragment.CHOOSE_FROM_LANG.code
            ) { fromLang ->
                fromLangCode(fromLang)
            }
                .subscribe({
                    _result.value = Result.Success(it)

                }, {
                    _result.value = Result.Error(Throwable())
                })
        )
    }

    fun translateWithoutRecognized(textForTranslate: String) {
        compositeDisposable.add(
            mlKitTranslateText.translateWithoutRecognizeLangs(
                text = textForTranslate,
                fromLang = ChooseFromLangDialogFragment.CHOOSE_FROM_LANG.code,
                toLang = ChooseToLangDialogFragment.CHOOSE_TO_LANG.code
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _result.value = Result.Success(it)

                }, {
                    _result.value = Result.Error(Throwable())
                })
        )
    }

    fun saveHistory(item: HistoryEntity) {
        compositeDisposable.add(
            Schedulers.io().createWorker().schedule {
                repository.saveHistory(item)
            }
        )
    }

    fun saveDictionary(item: DictionaryEntity) {
        compositeDisposable.add(repository.getAllDictionary()
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