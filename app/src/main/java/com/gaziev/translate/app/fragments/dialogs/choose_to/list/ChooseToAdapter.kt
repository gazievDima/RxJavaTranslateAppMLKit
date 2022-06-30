package com.gaziev.translate.app.fragments.dialogs.choose_to.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaziev.translate.R
import com.gaziev.translate.core.Language

class ChooseToAdapter(
    private val list: List<Language>,
    private val onClick: (codeLang: Language) -> Unit
) : RecyclerView.Adapter<ChooseToHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseToHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_choose_lang, parent, false)
        return ChooseToHolder(view)
    }

    override fun onBindViewHolder(holder: ChooseToHolder, position: Int) =
        holder.bind(list[position], onClick)

    override fun getItemCount(): Int = list.size
}