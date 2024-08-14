package org.smartregister.fct.sm.data.viewmodel

import androidx.compose.runtime.MutableState
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.hl7.fhir.r4.model.Resource
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.engine.util.encodeResourceToString
import org.smartregister.fct.engine.util.json
import org.smartregister.fct.engine.util.prettyJson
import org.smartregister.fct.sm.domain.model.SMDetail

class SMResultTabViewModel(
    private val resource: Resource,
    val codeController: CodeController = CodeController(
        runBlocking { resource.prettyJson() }
    )
)