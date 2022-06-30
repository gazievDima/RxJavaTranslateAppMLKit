package com.gaziev.vcttranslate.app.fragments.dialogs.choose_from.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaziev.vcttranslate.R
import com.gaziev.vcttranslate.core.Language

class ChooseFromAdapter(
    private val list: List<Language>,
    private val onClick: (codeLang: Language) -> Unit
) : RecyclerView.Adapter<ChooseFromHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseFromHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_choose_lang, parent, false)
        return ChooseFromHolder(view)
    }

    override fun onBindViewHolder(holder: ChooseFromHolder, position: Int) =
        holder.bind(list[position], onClick)

    override fun getItemCount(): Int = list.size
}