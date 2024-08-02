package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Code(var system: String? = null, var code: String? = null, var display: String? = null)