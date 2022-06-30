package com.gaziev.vcttranslate.app.fragments.photo

sealed class TypeTranslate {
    object FromLanguage: TypeTranslate()
    object ToLanguage : TypeTranslate()
}