package org.smartregister.fct.settings.presentation.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.aurora.domain.controller.DialogController
import org.smartregister.fct.aurora.ui.components.dialog.rememberDialog
import org.smartregister.fct.serverconfig.domain.model.DefaultSetting
import org.smartregister.fct.serverconfig.presentation.ui.components.Settings

@Composable
fun rememberSettingsDialog(
    componentContext: ComponentContext,
    defaultSetting: DefaultSetting = DefaultSetting.ServerConfigs,
    title: String = "Settings",
    cancelable: Boolean = true,
    onDismiss: (DialogController.() -> Unit)? = null,
): DialogController {

    val controller = rememberDialog(
        title = title,
        width = 1000.dp,
        height = 800.dp,
        cancelable = cancelable,
        onDismiss = onDismiss
    ) { _, _ ->
        Settings(
            componentContext = componentContext,
            defaultSetting = defaultSetting
        )
    }

    return controller
}