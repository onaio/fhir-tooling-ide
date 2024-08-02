
package org.smartregister.fct.configs.domain.model.view.property

import kotlinx.serialization.Serializable
import org.smartregister.fct.configs.data.serializer.ViewPropertiesSerializer
import org.smartregister.fct.configs.domain.model.ViewType
import java.util.LinkedList

/**
 * An abstract for view properties. This is needed so we can serialize/deserialize view properties
 * map into different data classes. Common view properties MUST be implemented by subclasses for
 * access.
 */
@Serializable(with = ViewPropertiesSerializer::class)
abstract class ViewProperties : java.io.Serializable {
  abstract val viewType: ViewType?
  abstract val weight: Float?
  abstract val backgroundColor: String?
  abstract val padding: Int?
  abstract val borderRadius: Int?
  abstract val alignment: ViewAlignment?
  abstract val fillMaxWidth: Boolean?
  abstract val fillMaxHeight: Boolean?
  abstract val clickable: String?
  abstract val visible: String?

  abstract fun interpolate(computedValuesMap: Map<String, Any>): ViewProperties
}

/**
 * This function obtains all [ListProperties] from the [ViewProperties] list; including the nested
 * LISTs
 */
fun List<ViewProperties>.retrieveListProperties(): List<ListProperties> {
  val listProperties = mutableListOf<ListProperties>()
  val viewPropertiesLinkedList: LinkedList<ViewProperties> = LinkedList(this)
  while (viewPropertiesLinkedList.isNotEmpty()) {
    val properties = viewPropertiesLinkedList.removeFirst()
    if (properties.viewType == ViewType.LIST) {
      listProperties.add(properties as ListProperties)
    }
    when (properties.viewType) {
      ViewType.COLUMN -> viewPropertiesLinkedList.addAll((properties as ColumnProperties).children ?: listOf())
      ViewType.ROW -> viewPropertiesLinkedList.addAll((properties as RowProperties).children ?: listOf())
      ViewType.CARD -> viewPropertiesLinkedList.addAll((properties as CardViewProperties).content ?: listOf())
      ViewType.LIST ->
        viewPropertiesLinkedList.addAll((properties as ListProperties).registerCard?.views ?: listOf())
      else -> {}
    }
  }
  return listProperties
}

enum class ViewAlignment {
  START,
  END,
  CENTER,
  NONE,
  TOPSTART,
  TOPEND,
  BOTTOMSTART,
  BOTTOMEND,
}
