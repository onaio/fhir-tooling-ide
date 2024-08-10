package org.smartregister.fct.sm.data.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import org.hl7.fhir.r4.model.Resource
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.engine.util.decodeResourceFromString
import org.smartregister.fct.logcat.FCTLogger
import org.smartregister.fct.sm.domain.model.SMDetail

class SMTabViewModel(
    var smDetail: SMDetail,
    val codeController: CodeController = CodeController(smDetail.body),
    val inputResource: MutableStateFlow<Resource?> = MutableStateFlow(
        try {
            smDetail.source.decodeResourceFromString<Resource>()
        } catch (ex: Throwable) {
            null
        }
    )
)