package org.smartregister.fct.configs.data.serializer

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.smartregister.fct.configs.domain.model.ViewType
import org.smartregister.fct.configs.domain.model.view.property.ButtonProperties
import org.smartregister.fct.configs.domain.model.view.property.CardViewProperties
import org.smartregister.fct.configs.domain.model.view.property.ColumnProperties
import org.smartregister.fct.configs.domain.model.view.property.CompoundTextProperties
import org.smartregister.fct.configs.domain.model.view.property.DividerProperties
import org.smartregister.fct.configs.domain.model.view.property.ImageProperties
import org.smartregister.fct.configs.domain.model.view.property.ListProperties
import org.smartregister.fct.configs.domain.model.view.property.PersonalDataProperties
import org.smartregister.fct.configs.domain.model.view.property.RowProperties
import org.smartregister.fct.configs.domain.model.view.property.ServiceCardProperties
import org.smartregister.fct.configs.domain.model.view.property.SpacerProperties
import org.smartregister.fct.configs.domain.model.view.property.StackViewProperties
import org.smartregister.fct.configs.domain.model.view.property.ViewProperties

private const val VIEW_TYPE = "viewType"

object ViewPropertiesSerializer :
  JsonContentPolymorphicSerializer<ViewProperties>(ViewProperties::class) {
  override fun selectDeserializer(
      element: JsonElement,
  ): DeserializationStrategy<out ViewProperties> {
    val jsonObject = element.jsonObject
    val viewType = jsonObject[VIEW_TYPE]?.jsonPrimitive?.content
    require(viewType != null && ViewType.values().contains(ViewType.valueOf(viewType))) {
      """Ensure that supported `viewType` property is included in your register view properties configuration.
         Supported types: ${ViewType.values()}
         Parsed JSON: $jsonObject
            """
        .trimMargin()
    }
    return when (ViewType.valueOf(viewType)) {
      ViewType.ROW -> RowProperties.serializer()
      ViewType.COLUMN -> ColumnProperties.serializer()
      ViewType.COMPOUND_TEXT -> CompoundTextProperties.serializer()
      ViewType.SERVICE_CARD -> ServiceCardProperties.serializer()
      ViewType.CARD -> CardViewProperties.serializer()
      ViewType.PERSONAL_DATA -> PersonalDataProperties.serializer()
      ViewType.BUTTON -> ButtonProperties.serializer()
      ViewType.SPACER -> SpacerProperties.serializer()
      ViewType.LIST -> ListProperties.serializer()
      ViewType.IMAGE -> ImageProperties.serializer()
      ViewType.BORDER -> DividerProperties.serializer()
      ViewType.STACK -> StackViewProperties.serializer()
    }
  }
}