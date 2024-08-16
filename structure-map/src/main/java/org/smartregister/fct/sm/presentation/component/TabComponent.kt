package org.smartregister.fct.sm.presentation.component

import com.arkivanov.decompose.value.Value
import org.hl7.fhir.r4.model.Bundle
import org.smartregister.fct.editor.data.controller.CodeController
import org.smartregister.fct.sm.domain.model.SMDetail

internal interface TabComponent{

    val smDetail: SMDetail
    val codeController: CodeController
    val groups: Value<List<String>>
    val outputResources: Value<List<String>>
    val sourceName: Value<String>

    fun save()
    suspend fun applyTransformation(): Result<Bundle>
    suspend fun addSource(source: String): Result<Unit>
}