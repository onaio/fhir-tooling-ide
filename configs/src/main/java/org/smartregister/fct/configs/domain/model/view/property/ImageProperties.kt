
package org.smartregister.fct.configs.domain.model.view.property

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.domain.model.ICON_TYPE_LOCAL
import org.smartregister.fct.configs.domain.model.ImageConfig
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class ImageProperties(
    override val viewType: ViewType = ViewType.IMAGE,
    override val weight: Float? = null,
    override val backgroundColor: String? = null,
    override val padding: Int? = null,
    override val borderRadius: Int? = null,
    override val alignment: ViewAlignment? = null,
    override val fillMaxWidth: Boolean? = null,
    override val fillMaxHeight: Boolean? = null,
    override val clickable: String? = null,
    override val visible: String? = null,
    val tint: String? = null,
    val text: String? = null,
    val imageConfig: ImageConfig? = null,
    val size: Int? = null,
    val shape: ImageShape? = null,
    val textColor: String? = null,
    val actions: List<org.smartregister.fct.configs.domain.model.ActionConfig>? = null,
) : ViewProperties() {
  override fun interpolate(computedValuesMap: Map<String, Any>): ViewProperties {
    return this.copy(
      visible = (visible ?: "true").interpolate(computedValuesMap),
      imageConfig =
        imageConfig?.copy(
          reference = imageConfig.reference?.interpolate(computedValuesMap),
          type = (imageConfig.type ?: ICON_TYPE_LOCAL).interpolate(computedValuesMap),
        ),
      tint = this.tint?.interpolate(computedValuesMap),
      backgroundColor = this.backgroundColor?.interpolate(computedValuesMap),
      text = this.text?.interpolate(computedValuesMap),
    )
  }
}

enum class ImageShape(val composeShape: Shape) {
  CIRCLE(CircleShape),
  RECTANGLE(RectangleShape),
}
