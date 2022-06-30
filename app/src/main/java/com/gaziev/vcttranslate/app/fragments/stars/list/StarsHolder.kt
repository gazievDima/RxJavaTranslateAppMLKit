package com.gaziev.vcttranslate.app.fragments.stars.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gaziev.vcttranslate.data.model.DictionaryEntity
import com.gaziev.vcttranslate.data.model.FavoriteEntity
import com.gaziev.vcttranslate.data.model.HistoryEntity
import com.gaziev.vcttranslate.databinding.ItemWordBinding

class StarsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemWordBinding.bind(itemView)

    fun bind(item: FavoriteEntity, callback: Callback) {
        binding.tvWord.text = item.word
        val code = "(${item.langCode})"
        binding.tvLanguage.text = code
        binding.btnVoice.setOnClickListener { callback.playVoice(item.word) }
        binding.btnCopy.setOnClickListener { callback.copy(item.word) }
    }
}