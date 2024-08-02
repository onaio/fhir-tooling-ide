package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EventTriggerCondition(
    val eventResourceId: String,
    val matchAll: Boolean? = true,
    val conditionalFhirPathExpressions: List<String>? = emptyList(),
)
