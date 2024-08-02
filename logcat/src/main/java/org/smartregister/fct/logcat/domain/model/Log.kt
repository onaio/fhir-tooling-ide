package org.smartregister.fct.logcat.domain.model

data class Log(
    val dateTime: String,
    val priority: LogLevel,
    val tag: String,
    val message: String
)