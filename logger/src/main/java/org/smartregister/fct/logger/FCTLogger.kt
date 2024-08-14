package org.smartregister.fct.logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.collections4.queue.CircularFifoQueue
import org.smartregister.fct.logger.model.Log
import org.smartregister.fct.logger.model.LogFilter
import org.smartregister.fct.logger.model.LogLevel
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object FCTLogger {

    private const val MAXIMUM_LOG_LIMIT = 1000
    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
    private var logFilter: LogFilter? = null
    private val isPause = MutableStateFlow(false)
    private val allLogs = MutableStateFlow<List<Log>>(listOf())
    private val logChain = CircularFifoQueue<Log>(MAXIMUM_LOG_LIMIT)
    private var priorityFilter: LogLevel? = null

    fun clearLogs() {
        CoroutineScope(Dispatchers.IO).launch {
            logChain.clear()
            allLogs.emit(listOf())
        }
    }

    fun togglePause() {
        CoroutineScope(Dispatchers.IO).launch {
            isPause.emit(!isPause.value)
        }
    }

    fun getAllLogs(): StateFlow<List<Log>> = allLogs

    fun getPause(): StateFlow<Boolean> = isPause

    fun clearPriorityFilter() {
        priorityFilter = null
        CoroutineScope(Dispatchers.IO).launch {
            allLogs.emit(logChain.toList())
        }
    }

    fun filterByPriority(priority: LogLevel) {
        priorityFilter = priority
        CoroutineScope(Dispatchers.IO).launch {
            allLogs.emit(
                logChain
                    .filter { it.priority == priorityFilter }
            )
        }
    }

    fun addFilter(logFilter: LogFilter) {
        FCTLogger.logFilter = logFilter
    }

    fun w(message: String, tag: String? = null, vararg args: Any?) {
        prepareLog(
            priority = LogLevel.WARNING,
            message = message,
            tag = tag,
            args = args
        )
    }

    fun v(message: String, tag: String? = null, vararg args: Any?) {
        prepareLog(
            priority = LogLevel.VERBOSE,
            message = message,
            tag = tag,
            args = args
        )
    }

    fun d(message: String, tag: String? = null, vararg args: Any?) {
        prepareLog(
            priority = LogLevel.DEBUG,
            message = message,
            tag = tag,
            args = args
        )
    }

    fun i(message: String, tag: String? = null, vararg args: Any?) {
        prepareLog(
            priority = LogLevel.INFO,
            message = message,
            tag = tag,
            args = args
        )
    }

    fun e(t: Throwable, tag: String? = null) {
        prepareLog(
            priority = LogLevel.ERROR,
            t = t,
            tag = tag,
        )
    }

    fun e(message: String, tag: String? = null, vararg args: Any?) {
        prepareLog(
            priority = LogLevel.ERROR,
            message = message,
            tag = tag,
            args = args
        )
    }

    fun wtf(message: String, tag: String? = null, vararg args: Any?) {
        prepareLog(
            priority = LogLevel.ASSERT,
            message = message,
            tag = tag,
            args = args
        )
    }

    private fun prepareLog(
        priority: LogLevel,
        message: String? = null,
        t: Throwable? = null,
        tag: String?,
        vararg args: Any?
    ) {
        var msg = message

        msg = if (msg.isNullOrEmpty()) {
            if (t == null) return
            getStackTraceString(t)
        } else {
            formatMessage(msg, args)
        }

        push(
            Log(
                dateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                tag = parseTag(tag),
                message = msg.replace("\t", "    "),
                priority = priority
            )
        )
    }

    private fun push(log: Log) {

        val isLoggable = logFilter?.isLoggable(log) ?: true
        if (!isLoggable || isPause.value) return

        CoroutineScope(Dispatchers.IO).launch {
            logChain.add(log)
            allLogs.emit(
                priorityFilter?.let { logChain.filter { it.priority == priorityFilter } }
                    ?: logChain.toList()
            )
        }
    }

    private fun formatMessage(message: String, args: Array<out Any?>) = message.format(*args)

    private fun parseTag(tag: String?): String {
        return tag ?: Throwable()
            .stackTrace
            .first { it.className !in listOf(FCTLogger::class.java.name) }
            .let(FCTLogger::createStackElementTag)
    }

    private fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        return tag
    }

    private fun getStackTraceString(t: Throwable): String {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}