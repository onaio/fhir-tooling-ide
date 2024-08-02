package org.smartregister.fct.logcat.domain.model

interface LogFilter {
    fun isLoggable(log: Log): Boolean
}