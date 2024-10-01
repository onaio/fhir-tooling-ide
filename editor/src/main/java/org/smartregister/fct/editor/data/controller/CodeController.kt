package org.smartregister.fct.editor.data.controller

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.engine.util.prettyJson
import org.smartregister.fct.logger.FCTLogger

class CodeController(
    scope: CoroutineScope,
    initialText: String = "",
    private val fileType: FileType? = null,
    private val readOnly: Boolean = false,
) : InstanceKeeper.Instance{

    private var text: String = initialText
    private val textFlow = MutableStateFlow(initialText)
    internal val initTextFlow = MutableStateFlow(initialText)
    internal val postInitTextFlow = MutableStateFlow(initialText)
    private var _fileType = fileType

    private val _isReadOnly = MutableStateFlow(readOnly)
    val isReadOnly: StateFlow<Boolean> = _isReadOnly

    internal var isInitialTextSet = false
        private set

    init {
        if (fileType == FileType.Json && initialText.isNotEmpty()) {
            scope.launch {
                try {
                    coroutineScope {
                        val prettyJson = initialText.prettyJson()
                        setText(prettyJson)
                        initTextFlow.emit(prettyJson)
                        isInitialTextSet = true
                    }
                } catch (ex: Exception) {
                    FCTLogger.e(ex)
                }
            }
        } else {
            isInitialTextSet = true
        }
    }

    fun getText() = text

    fun getTextAsFlow(): StateFlow<String> = textFlow

    internal suspend fun setText(text: String) {
        this.text = text
        textFlow.emit(text)
    }

    suspend fun setPostText(text: String) {
        this.text = text
        postInitTextFlow.emit(text)
        textFlow.emit(text)
    }

    fun setFileType(fileType: FileType?) {
        _fileType = fileType
    }

    fun getFileType() = _fileType
}