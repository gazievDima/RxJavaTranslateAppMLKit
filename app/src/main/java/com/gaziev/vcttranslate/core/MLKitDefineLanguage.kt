package com.gaziev.vcttranslate.core

import com.google.mlkit.nl.languageid.LanguageIdentification
import io.reactivex.rxjava3.core.Single

class MLKitDefineLanguage {

    fun getCodeLanguage(text: String): Single<String> {
        return Single.create { emmiter ->

            val languageIdentifier = LanguageIdentification.getClient()
            languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener { languageCode ->
                    if (languageCode == "und") {
                        emmiter.onError(Throwable())
                    } else {
                        emmiter.onSuccess(languageCode)
                    }
                }
                .addOnFailureListener {
                    emmiter.onError(Throwable())
                }
        }
    }

}