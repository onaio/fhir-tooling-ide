
package org.smartregister.fct.configs.domain.model.view.property

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class ServiceCardProperties(
    override val viewType: ViewType = ViewType.SERVICE_CARD,
    override val weight: Float? = null,
    override val backgroundColor: String? = null,
    override val padding: Int? = null,
    override val borderRadius: Int? = null,
    override val alignment: ViewAlignment? = null,
    override val fillMaxWidth: Boolean? = null,
    override val fillMaxHeight: Boolean? = null,
    override val clickable: String? = null,
    override val visible: String? = null,
    val details: List<CompoundTextProperties>? = null,
    val showVerticalDivider: Boolean? = null,
    val serviceMemberIcons: String? = null,
    val serviceButton: ButtonProperties? = null,
    val services: List<ButtonProperties>? = null,
    val actions: List<org.smartregister.fct.configs.domain.model.ActionConfig>? = null,
) : ViewProperties() {
  override fun interpolate(computedValuesMap: Map<String, Any>): ServiceCardProperties {
    return this.copy(
      backgroundColor = backgroundColor?.interpolate(computedValuesMap),
      visible = (visible ?: "true").interpolate(computedValuesMap),
      serviceMemberIcons = serviceMemberIcons?.interpolate(computedValuesMap),
      clickable = (clickable ?: "true").interpolate(computedValuesMap),
      details = details?.map { it.interpolate(computedValuesMap) },
      serviceButton = serviceButton?.interpolate(computedValuesMap),
      services = services?.map { it.interpolate(computedValuesMap) },
    )
  }
}
