package org.smartregister.fct.configs.domain.model.view.property

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class PersonalDataProperties(
    override val viewType: ViewType = ViewType.PERSONAL_DATA,
    override val weight: Float? = null,
    override val backgroundColor: String? = null,
    override val padding: Int? = null,
    override val borderRadius: Int? = null,
    override val alignment: ViewAlignment? = null,
    override val fillMaxWidth: Boolean? = null,
    override val fillMaxHeight: Boolean? = null,
    override val clickable: String? = null,
    override val visible: String? = null,
    val personalDataItems: List<PersonalDataItem>? = null,
) : ViewProperties() {
    override fun interpolate(computedValuesMap: Map<String, Any>): PersonalDataProperties {
        return this.copy(
            backgroundColor = backgroundColor?.interpolate(computedValuesMap),
            visible = (visible ?: "true").interpolate(computedValuesMap),
            personalDataItems =
            personalDataItems?.map {
                PersonalDataItem(
                    label = it.label.interpolate(computedValuesMap),
                    displayValue = it.displayValue.interpolate(computedValuesMap),
                )
            },
        )
    }
}

@Serializable
data class PersonalDataItem(
    val label: CompoundTextProperties,
    val displayValue: CompoundTextProperties,
)
