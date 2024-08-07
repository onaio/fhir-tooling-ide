package org.smartregister.fct.editor.data.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CodeController {

    private var text: String = ""
    private val textFlow = MutableStateFlow("")

    fun getText() = text

    fun getTextAsFlow(): StateFlow<String> = textFlow

    internal fun setText(scope: CoroutineScope, text: String) {
        this.text = text
        scope.launch {
            textFlow.emit(text)
        }
    }
}