package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterFilterConfig(
    val dataFilterActions: List<ActionConfig>? = null,
    val dataFilterFields: List<RegisterFilterField> = emptyList(),
)

@Serializable
data class RegisterFilterField(
    val filterId: String,
    val dataQueries: List<DataQuery>? = null,
    val nestedSearchResources: List<NestedSearchConfig>? = null,
)
