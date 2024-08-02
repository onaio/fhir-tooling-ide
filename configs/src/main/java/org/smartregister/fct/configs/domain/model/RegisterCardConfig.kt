
package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.view.property.ViewProperties

@Serializable
data class RegisterCardConfig(
    val rules: List<RuleConfig>? = null,
    val views: List<ViewProperties>? = null,
)
