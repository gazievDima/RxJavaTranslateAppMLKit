package com.gaziev.translate.app.fragments.stars

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaziev.translate.app.repository.FavoriteRepository
import com.gaziev.translate.data.model.DictionaryEntity
import com.gaziev.translate.data.model.FavoriteEntity
import com.gaziev.translate.data.model.HistoryEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class StarsViewModel(
    private val repository: FavoriteRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var _listWords: MutableLiveData<List<FavoriteEntity>> = MutableLiveData()
    val listWords: LiveData<List<FavoriteEntity>> = _listWords

    fun getHistory() {
        compositeDisposable.add(
            repository.getAllHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _listWords.value = it
                    }, {
                        Log.e(TAG, "Cannot get history!")
                    }
                )
        )
    }

    fun getDictionary() {
        compositeDisposable.add(
            repository.getAllDictionary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _listWords.value = it
                }, {
                    Log.e(TAG, "Cannot get dictionary!")
                })
        )
    }

    fun getNewList(list: List<FavoriteEntity>, searchWord: String): List<FavoriteEntity> {
        val newList: MutableList<FavoriteEntity> = mutableListOf()

        for (el in list) {
            if(compareWords(el.word, searchWord))
                when(el) {
                    is HistoryEntity -> { newList.add(HistoryEntity(word = el.word, langCode = el.langCode)) }
                    is DictionaryEntity -> { newList.add(DictionaryEntity(word = el.word, langCode = el.langCode)) }
                }
        }
        return newList
    }

    private fun compareWords(word: String, searchWord: String): Boolean =
        word.lowercase().contains(searchWord.lowercase())

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}