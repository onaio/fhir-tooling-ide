package org.smartregister.fct.configs.domain.model.view.property

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class CardViewProperties(
    override val viewType: ViewType? = null,
    override val weight: Float? = null,
    override val backgroundColor: String? = null,
    override val padding: Int? = null,
    override val borderRadius: Int? = null,
    override val alignment: ViewAlignment? = null,
    override val fillMaxWidth: Boolean? = null,
    override val fillMaxHeight: Boolean? = null,
    override val clickable: String? = null,
    override val visible: String? = null,
    val content: List<ViewProperties>? = null,
    val elevation: Int? = null,
    val cornerSize: Int? = null,
    val header: CompoundTextProperties? = null,
    val headerBackgroundColor: String? = null,
    val headerAction: CompoundTextProperties? = null,
    val emptyContentMessage: String? = null,
    val contentPadding: Int? = null,
) : ViewProperties() {
    override fun interpolate(computedValuesMap: Map<String, Any>): CardViewProperties {
        return this.copy(
            backgroundColor = backgroundColor?.interpolate(computedValuesMap),
            visible = (visible ?: "true").interpolate(computedValuesMap),
            headerAction = headerAction?.interpolate(computedValuesMap),
            header = header?.interpolate(computedValuesMap),
        )
    }
}
