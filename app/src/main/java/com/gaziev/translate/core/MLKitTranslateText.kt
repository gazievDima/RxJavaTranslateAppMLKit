package com.gaziev.translate.core

import android.content.ContentValues.TAG
import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import io.reactivex.rxjava3.core.Single
import java.util.*

class MLKitTranslateText {

    fun translateWithoutRecognizeLangs(
        text: String,
        fromLang: String,
        toLang: String
    ): Single<String> {
        return Single.create { emmiter ->

            val translatorClient = Translation.getClient(getOptions(fromLang, toLang))
            translatorClient.downloadModelIfNeeded(getConditions())
                .addOnSuccessListener {
                    translatorClient.translate(text)
                        .addOnSuccessListener { translatedText ->
                            Log.e(TAG, "SUCCESS!!!! $translatedText")
                            emmiter.onSuccess(translatedText)
                        }
                        .addOnFailureListener { error ->
                            emmiter.onError(error)
                            Log.e(TAG, "ERROR cannot translated, $error")
                        }
                }
                .addOnFailureListener { error ->
                    emmiter.onError(error)
                    Log.e(TAG, "ERROR cannot downloaded language module, $error")
                }
        }
    }

    fun translateWithRecognizeLangs(
        text: String,
        toLang: String,
        mlKitDefineLanguage: MLKitDefineLanguage,
        reserveFromLang: String,
        returnFromLangCode: (langCode: String) -> Unit
    ): Single<String> {
        return Single.create { emmiter ->
            mlKitDefineLanguage.getCodeLanguage(text)
                .subscribe({ fromLang ->

                    val loc = Locale(fromLang)
                    returnFromLangCode(loc.getDisplayLanguage(loc))

                    val translatorClient = Translation.getClient(getOptions(fromLang, toLang))
                    translatorClient.downloadModelIfNeeded(getConditions())
                        .addOnSuccessListener {
                            translatorClient.translate(text)
                                .addOnSuccessListener { translatedText ->
                                    emmiter.onSuccess(translatedText)
                                }
                                .addOnFailureListener { error ->
                                    emmiter.onError(error)
                                    Log.e(TAG, "ERROR cannot translated, $error")
                                }
                        }
                        .addOnFailureListener { error ->
                            emmiter.onError(error)
                            Log.e(TAG, "ERROR cannot downloaded language module, $error")
                        }
                }, { error ->
                    Log.e(TAG, "ERROR cannot get code language, $error")

                    val translatorClient = Translation.getClient(getOptions(reserveFromLang, toLang))
                    translatorClient.downloadModelIfNeeded(getConditions())
                        .addOnSuccessListener {
                            translatorClient.translate(text)
                                .addOnSuccessListener { translatedText ->
                                    emmiter.onSuccess(translatedText)
                                }
                                .addOnFailureListener { error ->
                                    emmiter.onError(error)
                                    Log.e(TAG, "ERROR cannot translated, $error")
                                }
                        }
                        .addOnFailureListener { errorDownloadLangModule ->
                            emmiter.onError(errorDownloadLangModule)
                            Log.e(TAG, "ERROR cannot downloaded language module, $error")
                        }
                })
        }
    }

    private fun getOptions(fromLang: String, toLang: String): TranslatorOptions {
        return TranslatorOptions.Builder()
            .setSourceLanguage(fromLang)
            .setTargetLanguage(toLang)
            .build()
    }

    private fun getConditions(): DownloadConditions {
        return DownloadConditions.Builder().build()
    }

}