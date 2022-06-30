package com.gaziev.vcttranslate.app

import android.app.Application
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_from.ChooseFromLangDialogFragment
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_to.ChooseToLangDialogFragment
import com.gaziev.vcttranslate.app.fragments.stars.StarsFragment
import com.gaziev.vcttranslate.core.SettingsSharedPrefs
import com.gaziev.vcttranslate.core.Language
import com.gaziev.vcttranslate.data.model.HistoryEntity
import com.gaziev.vcttranslate.data.source.local.room.RoomLocalBase

class VCTtranslate : Application() {
    companion object {
        lateinit var instance: VCTtranslate
    }

    private val database: RoomLocalBase by lazy {
        Room.databaseBuilder(this, RoomLocalBase::class.java, "database_main")
            .fallbackToDestructiveMigration()
            .build()
    }

    private lateinit var shareds: SharedPreferences
    private lateinit var settingsSharedPrefs: SettingsSharedPrefs

    override fun onCreate() {
        super.onCreate()
        instance = this

        shareds = getSharedPreferences("langs", MODE_PRIVATE)
        settingsSharedPrefs = SettingsSharedPrefs(shareds)
        settingsSharedPrefs.loadLangs()
        settingsSharedPrefs.loadHistory()
    }

    fun getDataBase(): RoomLocalBase = database
    fun getSharedPrefsSettings(): SettingsSharedPrefs = settingsSharedPrefs
    fun getShareds(): SharedPreferences = shareds

}