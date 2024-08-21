package org.smartregister.fct.common.presentation.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.smartregister.fct.aurora.data.locals.AuroraLocal
import org.smartregister.fct.aurora.domain.controller.DialogController
import org.smartregister.fct.aurora.domain.manager.AuroraManager
import org.smartregister.fct.aurora.domain.model.Message
import org.smartregister.fct.aurora.presentation.ui.components.Button
import org.smartregister.fct.aurora.presentation.ui.components.TextField
import org.smartregister.fct.aurora.presentation.ui.components.container.Aurora
import org.smartregister.fct.aurora.presentation.ui.components.dialog.rememberDialog
import org.smartregister.fct.common.data.locals.LocalSnackbarHost
import org.smartregister.fct.common.domain.model.ServerConfig
import org.smartregister.fct.common.presentation.component.UploadResourceDialogComponent

@Composable
fun rememberUploadResourceDialog(
    componentContext: ComponentContext,
    title: String = "Select Config",
    onDismiss: (DialogController<String>.() -> Unit)? = null,
): DialogController<String> {

    val dialogController = rememberDialog<String>(
        title = title,
        height = 350.dp,
        onDismiss = onDismiss,
    ) { _, data ->
        with(UploadResourceDialogComponent(componentContext, data!!)) {
            UploadResourcePanel()
        }
    }

    return dialogController
}

context (UploadResourceDialogComponent)
@Composable
private fun UploadResourcePanel() {
    Aurora {
        Content(configs.subscribeAsState().value) {
            UploadButton()
        }

        val aurora = AuroraLocal.current
        val success by uploadSuccess.collectAsState()
        val error by uploadError.collectAsState()

        success?.let { aurora?.showSnackbar(it) }
        error?.let { aurora?.showSnackbar(Message.Error(it)) }
    }
}

context (UploadResourceDialogComponent)
@Composable
private fun Content(
    configs: List<ServerConfig>,
    bottomContent: @Composable BoxScope.() -> Unit,
) {
    ConstraintLayout {
        val (list, button) = createRefs()

        Box(Modifier.padding(horizontal = 12.dp).constrainAs(list) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            bottom.linkTo(button.top)
            height = Dimension.preferredWrapContent
        }) {
            ConfigList(configs)
        }

        Column(
            Modifier.constrainAs(button) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
        ) {

            val resourceId by defaultResourceId.subscribeAsState()
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = resourceId,
                onValueChange = {
                    defaultResourceId.value = it
                },
                label = (resourceType ?: "") + " Id",
            )

            Box(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainer).padding(12.dp),
            ) {
                bottomContent()
            }
        }
    }
}

context (UploadResourceDialogComponent, BoxScope)
@Composable
private fun ConfigList(configs: List<ServerConfig>) {

    val selectedConfig by selectedConfig.collectAsState()

    // TODO scrollbar indicator not showing right now will need to figure out
    LazyColumn(Modifier.fillMaxSize()) {

        items(configs) { item ->

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedConfig?.id == item.id,
                    onClick = {
                        selectConfig(item)
                    }
                )
                Spacer(Modifier.width(8.dp))
                Text(item.title)
            }
        }
    }
}

context (UploadResourceDialogComponent, BoxScope)
@Composable
private fun UploadButton() {

    if (uploading.subscribeAsState().value) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp).align(Alignment.Center),
            strokeWidth = 2.dp
        )
    } else {
        Button(
            modifier = Modifier.fillMaxWidth(),
            label = "Upload",
            enable = selectedConfig.collectAsState().value != null && defaultResourceId
                .subscribeAsState().value.trim().isNotEmpty(),
            onClick = ::upload
        )
    }

}