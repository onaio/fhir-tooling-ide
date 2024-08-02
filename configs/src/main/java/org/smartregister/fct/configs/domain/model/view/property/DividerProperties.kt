package org.smartregister.fct.configs.domain.model.view.property

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class DividerProperties(
    override val viewType: ViewType = ViewType.BORDER,
    override val weight: Float? = null,
    override val backgroundColor: String? = null,
    override val padding: Int? = null,
    override val borderRadius: Int? = null,
    override val alignment: ViewAlignment? = null,
    override val fillMaxWidth: Boolean? = null,
    override val fillMaxHeight: Boolean? = null,
    override val clickable: String? = null,
    override val visible: String? = null,
    val thickness: Float? = null,
) : ViewProperties() {
    override fun interpolate(computedValuesMap: Map<String, Any>): DividerProperties {
        return this.copy(
            backgroundColor = backgroundColor?.interpolate(computedValuesMap),
            visible = (visible ?: "true").interpolate(computedValuesMap),
        )
    }
}
