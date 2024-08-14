package org.smartregister.fct.engine.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppSetting(
    val isDarkTheme: Boolean = false,
    val codeEditorConfig: CodeEditorConfig = CodeEditorConfig()
)
