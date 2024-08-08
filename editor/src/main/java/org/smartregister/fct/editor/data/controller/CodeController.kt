package org.smartregister.fct.editor.data.controller

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CodeController(initialText: String = "") {

    private var text: String = initialText
    private val textFlow = MutableStateFlow(initialText)

    fun getText() = text

    fun getTextAsFlow(): StateFlow<String> = textFlow

    suspend fun setText(text: String) {
        this.text = text
        textFlow.emit(text)
    }
}