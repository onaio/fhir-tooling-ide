package org.smartregister.fct.common.presentation.ui.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.smartregister.fct.aurora.presentation.ui.components.TextButton
import org.smartregister.fct.common.data.controller.DialogController
import org.smartregister.fct.common.presentation.component.ServerConfigProviderDialogComponent
import org.smartregister.fct.engine.domain.model.ServerConfig

@Composable
fun rememberServerConfigProviderDialog(
    componentContext: ComponentContext,
    title: String = "Select Config",
    cancelOnTouchOutside: Boolean = true,
    onDismiss: (DialogController<ServerConfig>.() -> Unit)? = null,
    onConfigSelected: (ServerConfig) -> Unit
): DialogController<ServerConfig> {

    val dialogController = rememberDialog(
        title = title,
        width = 250.dp,
        height = 300.dp,
        cancelOnTouchOutside = cancelOnTouchOutside,
        onDismiss = onDismiss,
    ) { controller, data ->
        with(ServerConfigProviderDialogComponent(componentContext, data)) {
            with(controller) {
                ConfigList(
                    onConfigSelected = onConfigSelected
                )
            }
        }
    }

    return dialogController
}

context (DialogController<ServerConfig>)
@Composable
private fun ServerConfigProviderDialogComponent.ConfigList(
    onConfigSelected: (ServerConfig) -> Unit
) {

    val configs by configs.subscribeAsState()

    // TODO scrollbar indicator not showing right now will need to figure out
    LazyColumn(Modifier.fillMaxSize().padding(vertical = 8.dp)) {

        items(configs) { item ->
            TextButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                label = item.title,
                textAlign = TextAlign.Start,
                selected = selectedConfig.collectAsState().value?.id == item.id,
                onClick = {
                    onConfigSelected(item)
                    hide()
                }
            )
        }
    }
}