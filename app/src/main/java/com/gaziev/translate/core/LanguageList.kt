package com.gaziev.translate.core

class Language(
    val code: String,
    val name: String
)

object LanguageList {
    val list: List<Language> = listOf(
        Language("af", "Afrikaans"),
        Language("sq", "Albanian"),
        Language("ar", "Arabic"),
        Language("be", "Belarusian"),
        Language("bg", "Bulgarian"),
        Language("bn", "Bengali"),
        Language("ca", "Catalan"),
        Language("ceb", "Cebuano"),
        Language("zh", "Chinese"),
        Language("co", "Corsican"),
        Language("cs", "Czech"),
        Language("en", "English"),
        Language("eo", "Esperanto"),
        Language("et", "Estonian"),
        Language("fi", "Finnish"),
        Language("de", "German"),
        Language("el", "Greek"),
        Language("gu", "Gujarati"),
        Language("ga", "Irish"),
        Language("lv", "Latvian"),
        Language("fa", "Persian"),
        Language("pt", "Portuguese"),
        Language("pl", "Polish"),
        Language("ru", "Russian"),
        Language("ro", "Romanian"),
        Language("es", "Spanish"),
        Language("sv", "Swedish"),
        Language("tr", "Turkish"),
        Language("uk", "Ukrainian"),
        Language("cy", "Welsh")
    )

}