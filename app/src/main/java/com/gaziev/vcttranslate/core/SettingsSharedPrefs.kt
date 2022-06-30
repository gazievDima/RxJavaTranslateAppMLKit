package com.gaziev.vcttranslate.core

import android.content.SharedPreferences
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_from.ChooseFromLangDialogFragment
import com.gaziev.vcttranslate.app.fragments.dialogs.choose_to.ChooseToLangDialogFragment
import com.gaziev.vcttranslate.app.fragments.stars.StarsFragment
import com.gaziev.vcttranslate.data.model.HistoryEntity

class SettingsSharedPrefs(
    private val shareds: SharedPreferences
) {

    fun saveLangs(langTo: Language, langFrom: Language) {
        shareds.edit().putString("to_lang_code", langTo.code).apply()
        shareds.edit().putString("to_lang_name", langTo.name).apply()

        shareds.edit().putString("from_lang_code", langFrom.code).apply()
        shareds.edit().putString("from_lang_name", langFrom.name).apply()
    }

    fun loadLangs() {
        val toLang = Language(
            shareds.getString("to_lang_code", "en") ?: "en",
            shareds.getString("to_lang_name", "English") ?: "English"
        )
        ChooseToLangDialogFragment.CHOOSE_TO_LANG = toLang

        val fromLang = Language(
            shareds.getString("from_lang_code", "en") ?: "en",
            shareds.getString("from_lang_name", "English") ?: "English"
        )
        ChooseFromLangDialogFragment.CHOOSE_FROM_LANG = fromLang
    }

    fun loadHistory() {
        val lastHistory = HistoryEntity(
            word = shareds.getString("last_word_history", "empty") ?: "empty",
            langCode = shareds.getString("last_code_history", "empty") ?: "empty"
        )
        StarsFragment.LAST_WORD_FROM_HISTORY = lastHistory
    }

}