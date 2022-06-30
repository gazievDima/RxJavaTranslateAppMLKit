package com.gaziev.vcttranslate.app.fragments.stars.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaziev.vcttranslate.R
import com.gaziev.vcttranslate.data.model.DictionaryEntity
import com.gaziev.vcttranslate.data.model.HistoryEntity
import com.gaziev.vcttranslate.data.model.FavoriteEntity

class StarsAdapter(
    private val list: List<FavoriteEntity>,
    private val callback: Callback,
) : RecyclerView.Adapter<StarsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarsHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return StarsHolder(view)
    }

    override fun onBindViewHolder(holder: StarsHolder, position: Int) {
        holder.bind(list[position], callback)
    }

    override fun getItemCount(): Int = list.size
}