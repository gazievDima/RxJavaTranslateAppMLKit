package com.gaziev.vcttranslate.core

import android.text.Editable
import android.text.TextWatcher
import io.reactivex.rxjava3.subjects.BehaviorSubject

class SimpleTextWatcher() : TextWatcher {

    val behaviorSubject: BehaviorSubject<String> = BehaviorSubject.create()

    override fun beforeTextChanged(data: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
    override fun onTextChanged(data: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
    override fun afterTextChanged(data: Editable?) {
        behaviorSubject.onNext(data.toString())
    }

}