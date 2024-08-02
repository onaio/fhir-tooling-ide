package org.smartregister.fct.configs.domain.model.view.property

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class StackViewProperties(
    override val viewType: ViewType = ViewType.STACK,
    override val weight: Float? = null,
    override val backgroundColor: String? = null,
    override val padding: Int? = null,
    override val borderRadius: Int? = null,
    override val alignment: ViewAlignment? = null,
    override val fillMaxWidth: Boolean? = null,
    override val fillMaxHeight: Boolean? = null,
    override val clickable: String? = null,
    override val visible: String? = null,
    val opacity: Float? = null,
    val size: Int? = null,
    val children: List<ViewProperties>? = null,
) : ViewProperties() {
    override fun interpolate(computedValuesMap: Map<String, Any>): StackViewProperties {
        return this.copy(
            backgroundColor = backgroundColor?.interpolate(computedValuesMap),
            visible = (visible ?: "true").interpolate(computedValuesMap),
        )
    }
}
