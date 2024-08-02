package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class EventWorkflow(
    val eventType: EventType = EventType.RESOURCE_CLOSURE,
    val triggerConditions: List<EventTriggerCondition> = emptyList(),
    val eventResources: List<ResourceConfig> = emptyList(),
    val updateValues: List<UpdateWorkflowValueConfig> = emptyList(),
    val resourceFilterExpressions: List<ResourceFilterExpression>? = null,
)

@Serializable
data class UpdateWorkflowValueConfig(
    val jsonPathExpression: String,
    //val value: JsonElement,
    //val resourceType: ResourceType = ResourceType.Task,
) {
    /*
    constructor(
        parcel: Parcel,
    ) : this(
        //parcel.readString() ?: "",
        //Json.decodeFromString(parcel.readString() ?: ""),
        //ResourceType.fromCode(parcel.readString()),
    )

      override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(jsonPathExpression)
        parcel.writeString(value.toString())
        parcel.writeString(resourceType.name)
      }

      override fun describeContents(): Int {
        return 0
      }

      companion object CREATOR : Parcelable.Creator<UpdateWorkflowValueConfig> {
        override fun createFromParcel(parcel: Parcel): UpdateWorkflowValueConfig {
          return UpdateWorkflowValueConfig(parcel)
        }

        override fun newArray(size: Int): Array<UpdateWorkflowValueConfig?> {
          return arrayOfNulls(size)
        }
      }*/
}

