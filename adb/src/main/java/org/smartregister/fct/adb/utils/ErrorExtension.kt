package org.smartregister.fct.adb.utils

import org.smartregister.fct.adb.data.exception.CommandException

fun<T> Throwable?.asResult(t: Throwable? = null) : Result<T> = Result.failure(this ?: t ?: UnknownError())

fun<T> String.resultAsCommandException() = Result.failure<T>(CommandException(this))

fun String.takeIfNotError() = takeUnless { it.contains("error:") }