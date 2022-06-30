package com.gaziev.vcttranslate.app.view

import android.app.Activity
import androidx.appcompat.widget.Toolbar
import com.gaziev.vcttranslate.R

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