package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterContentConfig(
    val separator: String? = null,
    val display: String? = null,
    val rules: List<RuleConfig>? = null,
    val visible: Boolean? = null,
    val computedRules: List<String>? = null,
)
