package com.gaziev.translate.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gaziev.translate.data.model.DictionaryEntity
import com.gaziev.translate.data.model.HistoryEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface StarsDao {

    @Query("SELECT * FROM dictionary_table")
    fun getAllDictionary(): Single<List<DictionaryEntity>>

    @Insert
    fun saveDictionary(item: DictionaryEntity)

    @Query("SELECT * FROM history_table")
    fun getAllHistory(): Single<List<HistoryEntity>>

    @Insert
    fun saveHistory(item: HistoryEntity)

}