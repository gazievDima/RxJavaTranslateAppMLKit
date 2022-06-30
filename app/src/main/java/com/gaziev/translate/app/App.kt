package com.gaziev.translate.app

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.gaziev.translate.core.SettingsSharedPrefs
import com.gaziev.translate.data.source.local.room.RoomLocalBase

class App : Application() {
    companion object {
        lateinit var instance: App
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