package org.smartregister.fct.editor.data.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.editor.util.prettyJson
import org.smartregister.fct.logger.FCTLogger

class CodeController(scope: CoroutineScope, initialText: String = "", private val fileType: FileType? = null) {

    private var text: String = initialText
    private val textFlow = MutableStateFlow(initialText)
    internal val initTextFlow = MutableStateFlow(initialText)

    init {
        if (fileType == FileType.Json && initialText.isNotEmpty()) {
            scope.launch {
                try {
                    coroutineScope {
                        val prettyJson = initialText.prettyJson()
                        setText(prettyJson)
                        initTextFlow.emit(prettyJson)
                    }
                } catch (ex: Exception) {
                    FCTLogger.e(ex)
                }
            }
        }
    }

    fun getText() = text

    fun getTextAsFlow(): StateFlow<String> = textFlow

    suspend fun setText(text: String) {
        this.text = text
        textFlow.emit(text)
    }

    fun getFileType() = fileType
}