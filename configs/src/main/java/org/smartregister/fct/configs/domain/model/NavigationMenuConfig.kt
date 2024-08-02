package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class NavigationMenuConfig(
    val id: String,
    val visible: Boolean = true,
    val enabled: String = "true",
    val menuIconConfig: ImageConfig? = null,
    val display: String,
    val showCount: Boolean = false,
    val animate: Boolean = true,
    val actions: List<org.smartregister.fct.configs.domain.model.ActionConfig>? = null,
)

@Serializable
data class ImageConfig(
    var type: String? = null,
    val reference: String? = null,
    val color: String? = null,
    val alpha: Float? = null,
    val imageType: ImageType? = null,
    val contentScale: ContentScaleType? = null,
) {
    fun interpolate(computedValuesMap: Map<String, Any>): ImageConfig {
        return this.copy(
            reference = this.reference?.interpolate(computedValuesMap),
            type = (this.type ?: ICON_TYPE_LOCAL).interpolate(computedValuesMap),
        )
    }
}

const val ICON_TYPE_LOCAL = "local"
const val ICON_TYPE_REMOTE = "remote"

enum class ImageType {
    JPEG,
    PNG,
    SVG,
}

enum class ContentScaleType {
    FIT,
    CROP,
    FILLHEIGHT,
    INSIDE,
    NONE,
    FILLBOUNDS,
}
