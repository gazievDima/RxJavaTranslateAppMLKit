package com.gaziev.translate.app.view

import android.app.Activity
import androidx.appcompat.widget.Toolbar
import com.gaziev.translate.R

class MainToolbar(
    private val toolbar: Toolbar,
    private val activity: Activity
) {

    fun setup() {
        toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.btnCloseApp -> {
                    activity.finish()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

}