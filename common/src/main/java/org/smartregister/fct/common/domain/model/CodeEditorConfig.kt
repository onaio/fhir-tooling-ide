package org.smartregister.fct.common.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CodeEditorConfig(
    val indent: Int = 4
)