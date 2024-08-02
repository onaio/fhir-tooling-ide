package org.smartregister.fct.configs.domain.model

enum class QuestionnaireType {
    DEFAULT,
    EDIT,
    READ_ONLY,
}

fun QuestionnaireConfig.isDefault() =
    QuestionnaireType.valueOf(this.type) == QuestionnaireType.DEFAULT

fun QuestionnaireConfig.isEditable() =
    QuestionnaireType.valueOf(this.type) == QuestionnaireType.EDIT

fun QuestionnaireConfig.isReadOnly() =
    QuestionnaireType.valueOf(this.type) == QuestionnaireType.READ_ONLY
