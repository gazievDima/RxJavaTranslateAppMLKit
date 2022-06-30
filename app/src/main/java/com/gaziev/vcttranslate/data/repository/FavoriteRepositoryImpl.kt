package com.gaziev.vcttranslate.data.repository

import com.gaziev.vcttranslate.app.repository.FavoriteRepository
import com.gaziev.vcttranslate.data.model.DictionaryEntity
import com.gaziev.vcttranslate.data.model.HistoryEntity
import com.gaziev.vcttranslate.data.source.local.room.StarsDao
import io.reactivex.rxjava3.core.Single

class FavoriteRepositoryImpl(
    private val starsDao: StarsDao
) : FavoriteRepository {

    override fun getAllHistory(): Single<List<HistoryEntity>> = starsDao.getAllHistory()
    override fun saveHistory(item: HistoryEntity) = starsDao.saveHistory(item)

    override fun getAllDictionary(): Single<List<DictionaryEntity>> = starsDao.getAllDictionary()
    override fun saveDictionary(item: DictionaryEntity) = starsDao.saveDictionary(item)

}