package com.gaziev.translate.app.fragments.photo

sealed class TypeTranslate {
    object FromLanguage: TypeTranslate()
    object ToLanguage : TypeTranslate()
}