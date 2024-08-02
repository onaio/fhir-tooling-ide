package org.smartregister.fct.configs.domain.model

import androidx.compose.material3.SnackbarDuration
import kotlinx.serialization.Serializable

@Serializable
data class SnackBarMessageConfig(
    val message: String = "",
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val snackBarActions: List<org.smartregister.fct.configs.domain.model.ActionConfig> = emptyList(),
)
