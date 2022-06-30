package com.gaziev.vcttranslate.app.repository

import com.gaziev.vcttranslate.data.model.DictionaryEntity
import com.gaziev.vcttranslate.data.model.HistoryEntity
import io.reactivex.rxjava3.core.Single

interface FavoriteRepository {

    fun getAllHistory(): Single<List<HistoryEntity>>
    fun saveHistory(item: HistoryEntity)
    fun getAllDictionary(): Single<List<DictionaryEntity>>
    fun saveDictionary(item: DictionaryEntity)

}