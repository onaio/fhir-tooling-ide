package org.smartregister.fct.sm.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Resource
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.sm.data.enums.ResultType
import org.smartregister.fct.sm.domain.model.SMDetail

internal interface ResultTabComponent : ComponentContext{

    val resource: Resource
    val codeController: CodeController
}