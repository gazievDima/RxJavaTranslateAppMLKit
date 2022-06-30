package com.gaziev.vcttranslate.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary_table")
class DictionaryEntity(

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int? = null,

    @ColumnInfo(name = "word")
    override val word: String,

    @ColumnInfo(name = "lang_code")
    override val langCode: String

) : FavoriteEntity()