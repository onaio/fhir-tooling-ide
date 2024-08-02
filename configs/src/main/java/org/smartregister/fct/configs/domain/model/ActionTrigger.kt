package org.smartregister.fct.configs.domain.model

/** An application event that can trigger a workflow. Examples button click, on back press etc */
enum class ActionTrigger {
    /**
     * An action that is performed when user presses a button or any actionable component in the UI
     */
    ON_CLICK,

    /** Action that is triggered to count register items */
    ON_COUNT,

    /** Action that is triggered when a Questionnaire has been submitted */
    ON_QUESTIONNAIRE_SUBMISSION,
}
