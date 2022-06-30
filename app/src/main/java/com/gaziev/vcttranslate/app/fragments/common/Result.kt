package com.gaziev.vcttranslate.app.fragments.common

sealed class Result {
    class Success(val data: Any) : Result()
    class Error(val error: Throwable) : Result()
}