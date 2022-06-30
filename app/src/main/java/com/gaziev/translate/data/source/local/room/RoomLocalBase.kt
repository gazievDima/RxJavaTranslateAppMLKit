package com.gaziev.translate.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gaziev.translate.data.model.DictionaryEntity
import com.gaziev.translate.data.model.HistoryEntity

@Database(entities = [HistoryEntity::class, DictionaryEntity::class], version = 2)
abstract class RoomLocalBase : RoomDatabase() {
    abstract fun getDao(): StarsDao
}