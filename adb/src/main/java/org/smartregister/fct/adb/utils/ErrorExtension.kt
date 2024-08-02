package org.smartregister.fct.adb.utils

fun<T> Throwable?.asResult(t: Throwable? = null) : Result<T> = Result.failure(this ?: t ?: UnknownError())

