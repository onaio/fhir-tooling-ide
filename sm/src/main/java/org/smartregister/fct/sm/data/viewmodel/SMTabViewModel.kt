package org.smartregister.fct.sm.data.viewmodel

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import org.hl7.fhir.r4.model.Resource
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.sm.domain.model.SMDetail

class SMTabViewModel(
    var smDetail: SMDetail,
    val codeController: CodeController = CodeController(smDetail.body),
    var inputResource: Resource? = null
) {
}