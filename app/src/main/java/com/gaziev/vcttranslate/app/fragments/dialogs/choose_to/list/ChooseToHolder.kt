package com.gaziev.vcttranslate.app.fragments.dialogs.choose_to.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gaziev.vcttranslate.core.Language
import com.gaziev.vcttranslate.databinding.ItemChooseLangBinding

class ChooseToHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding = ItemChooseLangBinding.bind(itemView)

    fun bind(language: Language, onClick: (codeLang: Language) -> Unit) {
        binding.tvLanguage.apply {
            paint.isUnderlineText = true
            text = language.name
            setOnClickListener { onClick(language) }
        }

    }
}