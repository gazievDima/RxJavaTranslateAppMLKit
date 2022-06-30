package com.gaziev.translate.app.repository

import com.gaziev.translate.data.model.DictionaryEntity
import com.gaziev.translate.data.model.HistoryEntity
import io.reactivex.rxjava3.core.Single

interface FavoriteRepository {

    fun getAllHistory(): Single<List<HistoryEntity>>
    fun saveHistory(item: HistoryEntity)
    fun getAllDictionary(): Single<List<DictionaryEntity>>
    fun saveDictionary(item: DictionaryEntity)

}