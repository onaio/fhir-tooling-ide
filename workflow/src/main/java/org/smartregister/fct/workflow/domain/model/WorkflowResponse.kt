package org.smartregister.fct.workflow.domain.model

import kotlinx.serialization.Serializable
import org.smartregister.fct.workflow.data.enums.WorkflowType

@Serializable
internal data class WorkflowResponse(
    var error: String?,
    val result: List<String> = listOf()
)
