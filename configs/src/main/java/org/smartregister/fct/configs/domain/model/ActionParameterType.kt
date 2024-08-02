package org.smartregister.fct.configs.domain.model

/** Represents different types of parameters that can be defined within the config actions */
@Suppress("EXPLICIT_SERIALIZABLE_IS_REQUIRED")
enum class ActionParameterType {
    /** Represents parameters that are used to pre-populate Questionnaire items with initial values */
    PREPOPULATE,
    PARAMDATA,
    UPDATE_DATE_ON_EDIT,
    QUESTIONNAIRE_RESPONSE_POPULATION_RESOURCE,
    RESOURCE_ID,
}
