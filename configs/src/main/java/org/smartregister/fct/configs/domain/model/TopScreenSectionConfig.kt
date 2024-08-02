package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.view.property.ImageProperties

@Serializable
data class TopScreenSectionConfig(
    val searchBar: RegisterContentConfig? = null,
    val title: String? = null,
    val menuIcons: List<ImageProperties>? = null,
)
