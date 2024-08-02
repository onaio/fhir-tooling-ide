package org.smartregister.fct.configs.domain.model.view.property

import androidx.compose.foundation.layout.Arrangement
import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class ColumnProperties(
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
    val spacedBy: Int? = null,
    val wrapContent: Boolean? = null,
    val arrangement: ColumnArrangement? = null,
    val children: List<ViewProperties>? = null,
    val showDivider: String? = null,
    val actions: List<org.smartregister.fct.configs.domain.model.ActionConfig>? = null,
) : ViewProperties() {
    override fun interpolate(computedValuesMap: Map<String, Any>): ColumnProperties {
        return this.copy(
            backgroundColor = backgroundColor?.interpolate(computedValuesMap),
            visible = (visible ?: "true").interpolate(computedValuesMap),
            showDivider = (showDivider ?: "false").interpolate(computedValuesMap),
        )
    }
}

enum class ColumnArrangement(val position: Arrangement.Vertical) {
    SPACE_BETWEEN(Arrangement.SpaceBetween),
    SPACE_AROUND(Arrangement.SpaceAround),
    SPACE_EVENLY(Arrangement.SpaceEvenly),
    CENTER(Arrangement.Center),
    TOP(Arrangement.Top),
    BOTTOM(Arrangement.Bottom),
}
