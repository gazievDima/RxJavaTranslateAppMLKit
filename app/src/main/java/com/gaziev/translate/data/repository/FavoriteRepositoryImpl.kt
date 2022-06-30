package com.gaziev.translate.data.repository

import com.gaziev.translate.app.repository.FavoriteRepository
import com.gaziev.translate.data.model.DictionaryEntity
import com.gaziev.translate.data.model.HistoryEntity
import com.gaziev.translate.data.source.local.room.StarsDao
import io.reactivex.rxjava3.core.Single

class FavoriteRepositoryImpl(
    private val starsDao: StarsDao
) : FavoriteRepository {

    override fun getAllHistory(): Single<List<HistoryEntity>> = starsDao.getAllHistory()
    override fun saveHistory(item: HistoryEntity) = starsDao.saveHistory(item)

    override fun getAllDictionary(): Single<List<DictionaryEntity>> = starsDao.getAllDictionary()
    override fun saveDictionary(item: DictionaryEntity) = starsDao.saveDictionary(item)

}