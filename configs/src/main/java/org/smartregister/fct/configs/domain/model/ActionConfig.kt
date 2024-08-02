package org.smartregister.fct.configs.domain.model

import kotlinx.serialization.Serializable
import org.hl7.fhir.r4.model.Enumerations
import org.hl7.fhir.r4.model.ResourceType
import org.smartregister.fct.configs.util.extension.interpolate

@Serializable
data class ActionConfig(
    val trigger: ActionTrigger? = null,
    val workflow: String? = null,
    val id: String? = null,
    val display: String? = null,
    val rules: List<RuleConfig>? = null,
    val questionnaire: QuestionnaireConfig? = null,
    val managingEntity: ManagingEntityConfig? = null,
    val params: List<ActionParameter>? = null,
    val resourceConfig: FhirResourceConfig? = null,
    val toolBarHomeNavigation: ToolBarHomeNavigation? = null,
    val popNavigationBackStack: Boolean? = null,
    val multiSelectViewConfig: MultiSelectViewConfig? = null,
) {
    /*  fun paramsBundle(computedValuesMap: Map<String, Any> = emptyMap()): Bundle =
        Bundle().apply {
          params
            .filter { !it.paramType?.name.equals(PREPOPULATE_PARAM_TYPE) }
            .forEach { putString(it.key, it.value.interpolate(computedValuesMap)) }
        }*/

    fun interpolate(computedValuesMap: Map<String, Any>): ActionConfig =
        this.copy(
            id = id?.interpolate(computedValuesMap),
            workflow = workflow?.interpolate(computedValuesMap),
            display = display?.interpolate(computedValuesMap),
            managingEntity =
            managingEntity?.copy(
                dialogTitle = managingEntity.dialogTitle?.interpolate(computedValuesMap),
                dialogWarningMessage =
                managingEntity.dialogWarningMessage?.interpolate(computedValuesMap),
                dialogContentMessage =
                managingEntity.dialogContentMessage?.interpolate(computedValuesMap),
                noMembersErrorMessage =
                managingEntity.noMembersErrorMessage.interpolate(computedValuesMap),
                managingEntityReassignedMessage =
                managingEntity.managingEntityReassignedMessage.interpolate(computedValuesMap),
            ),
            params = params?.map { it.interpolate(computedValuesMap) },
        )

    companion object {
        const val PREPOPULATE_PARAM_TYPE = "PREPOPULATE"
    }
}

@Serializable
data class ActionParameter(
    val key: String,
    val paramType: ActionParameterType? = null,
    val dataType: Enumerations.DataType? = null,
    val value: String,
    val linkId: String? = null,
    val resourceType: ResourceType? = null,
) {

    fun interpolate(computedValuesMap: Map<String, Any>) =
        this.copy(
            value = value.interpolate(computedValuesMap),
            key = key.interpolate(computedValuesMap),
            linkId = linkId?.interpolate(computedValuesMap),
        )
}
