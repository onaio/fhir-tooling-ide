package org.smartregister.fct.engine.domain.model

import kotlinx.serialization.Serializable
import org.smartregister.fct.engine.domain.model.ServerConfig

@Serializable
data class AppSetting(
    val isDarkTheme: Boolean = false,
    val codeEditorConfig: CodeEditorConfig = CodeEditorConfig(),
    val serverConfigs: List<ServerConfig> = listOf()
)
