package org.smartregister.fct.configs.domain.model.view.property

import kotlinx.serialization.Serializable
import org.hl7.fhir.r4.model.ResourceType
import org.smartregister.fct.configs.domain.model.NoResultsConfig
import org.smartregister.fct.configs.domain.model.RegisterCardConfig
import org.smartregister.fct.configs.domain.model.SortConfig
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class ListProperties(
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
    val id: String? = null,
    val registerCard: RegisterCardConfig? = null,
    val showDivider: Boolean? = null,
    val emptyList: NoResultsConfig? = null,
    val orientation: ListOrientation? = null,
    val resources: List<ListResource>? = null,
) : ViewProperties() {
    override fun interpolate(computedValuesMap: Map<String, Any>): ListProperties {
        return this.copy(
            backgroundColor = backgroundColor?.interpolate(computedValuesMap),
            visible = (visible ?: "true").interpolate(computedValuesMap),
        )
    }
}

enum class ListOrientation {
    VERTICAL,
    HORIZONTAL,
}

@Serializable
data class ListResource(
    val id: String? = null,
    val relatedResourceId: String? = null,
    val resourceType: ResourceType,
    val conditionalFhirPathExpression: String? = null,
    val sortConfig: SortConfig? = null,
    val fhirPathExpression: String? = null,
    val relatedResources: List<ListResource> = emptyList(),
)
