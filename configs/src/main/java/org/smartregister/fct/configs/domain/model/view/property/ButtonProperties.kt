package org.smartregister.fct.configs.domain.model.view.property

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ImageConfig
import org.smartregister.fct.configs.domain.model.ServiceStatus
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class ButtonProperties(
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
    val contentColor: String? = null,
    val enabled: String? = null,
    val text: String? = null,
    val status: String? = null,
    val fontSize: Float? = null,
    val actions: List<org.smartregister.fct.configs.domain.model.ActionConfig>? = null,
    val buttonType: ButtonType? = null,
    val startIcon: ImageConfig? = null,
    val letterSpacing: Int? = null,
    val backgroundOpacity: Float? = null,
    val colorOpacity: Float? = null,
    val statusIconSize: Int? = null,
) : ViewProperties() {
    /**
     * This function determines the status color to display depending on the value of the service
     * status
     *
     * @property computedValuesMap Contains data extracted from the resources to be used on the UI
     */
    /*fun statusColor(computedValuesMap: Map<String, Any>): Color {
      return when (interpolateStatus(computedValuesMap)) {
        ServiceStatus.DUE -> InfoColor
        ServiceStatus.OVERDUE -> DangerColor
        ServiceStatus.UPCOMING -> DefaultColor
        ServiceStatus.COMPLETED -> DefaultColor
        ServiceStatus.IN_PROGRESS -> WarningColor
        ServiceStatus.EXPIRED -> DefaultColor
        ServiceStatus.FAILED -> DangerColor
      }
    }*/

    override fun interpolate(computedValuesMap: Map<String, Any>): ButtonProperties {
        return this.copy(
            backgroundColor = backgroundColor?.interpolate(computedValuesMap),
            visible = (visible ?: "true").interpolate(computedValuesMap),
            status = interpolateStatus(computedValuesMap).name,
            text = text?.interpolate(computedValuesMap),
            enabled = (enabled ?: "true").interpolate(computedValuesMap),
            clickable = (clickable ?: "false").interpolate(computedValuesMap),
            contentColor = contentColor?.interpolate(computedValuesMap),
            startIcon = startIcon?.interpolate(computedValuesMap),
        )
    }

    private fun interpolateStatus(computedValuesMap: Map<String, Any>): ServiceStatus {
        val interpolated = (this.status ?: ServiceStatus.DUE.name).interpolate(computedValuesMap)
        return if (ServiceStatus.entries.map { it.name }.contains(interpolated)) {
            ServiceStatus.valueOf(interpolated)
        } else ServiceStatus.UPCOMING
    }
}

enum class ButtonType {
    TINY,
    MEDIUM,
    BIG,
}
