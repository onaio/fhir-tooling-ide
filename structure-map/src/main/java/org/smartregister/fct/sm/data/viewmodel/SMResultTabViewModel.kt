package org.smartregister.fct.sm.data.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.editor.data.enums.FileType

class SMResultTabViewModel(
    resource: String,
) {

    val codeController: CodeController

    init {
        val scope = CoroutineScope(Dispatchers.Default)
        codeController = CodeController(scope, resource, FileType.Json)
    }
}