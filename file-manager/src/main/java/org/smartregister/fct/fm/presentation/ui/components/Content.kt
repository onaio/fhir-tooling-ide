package org.smartregister.fct.fm.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okio.Path
import org.smartregister.fct.aurora.domain.controller.ConfirmationDialogController
import org.smartregister.fct.aurora.ui.components.dialog.DialogType
import org.smartregister.fct.aurora.ui.components.dialog.rememberAlertDialog
import org.smartregister.fct.aurora.ui.components.dialog.rememberConfirmationDialog
import org.smartregister.fct.aurora.util.getOrDefault
import org.smartregister.fct.aurora.util.getOrThrow
import org.smartregister.fct.common.util.getKoinInstance
import org.smartregister.fct.fm.data.communication.InterCommunication
import org.smartregister.fct.fm.domain.model.ContextMenu
import org.smartregister.fct.fm.domain.model.ContextMenuType
import org.smartregister.fct.fm.presentation.components.FileManagerComponent

@Composable
internal fun ConstraintLayoutScope.Content(
    pathRef: ConstrainedLayoutReference,
    contentRef: ConstrainedLayoutReference,
    component: FileManagerComponent,
    contentOption: @Composable ColumnScope.() -> Unit,
) {
    val activePathContent by component.getActivePathContent().collectAsState()
    val verticalScrollState = rememberScrollState()
    val visible by component.visibleItem.collectAsState()

    val deleteErrorDialog = rememberAlertDialog(
        title = "Error",
        dialogType = DialogType.Error
    ) {
        val error =
            it.getExtra().getOrDefault<Exception?, String>(0, "Unknown Error") { ex, default ->
                ex?.message ?: default
            }
        Text(error)
    }

    val deleteDialog = rememberConfirmationDialog { controller, extras ->
        val contextMenu = extras.getOrThrow<ContextMenu>(0)
        val path = extras.getOrThrow<Path>(1)
        val result = component.onContextMenuClick(contextMenu, path)
        if (result.isFailure) {
            deleteErrorDialog.show(result.exceptionOrNull())
        }
    }

    Column(
        modifier = Modifier.constrainAs(contentRef) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            bottom.linkTo(pathRef.top)
            height = Dimension.preferredWrapContent
        }
    ) {
        contentOption(this)
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(verticalScrollState),
            ) {
                Items(
                    component = component,
                    visible = visible,
                    activePathContent = activePathContent,
                    deleteDialog = deleteDialog
                )
                Spacer(Modifier.height(12.dp))
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = verticalScrollState
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Items(
    component: FileManagerComponent,
    visible: Boolean,
    activePathContent: List<Path>,
    deleteDialog: ConfirmationDialogController
) {
    val scope = rememberCoroutineScope()
    Spacer(Modifier.height(12.dp))
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FlowRow(
            modifier = Modifier.fillMaxSize(),
        ) {
            activePathContent.forEach { path ->
                ContentItem(
                    path = path,
                    contextMenuList = component.getContextMenuList(),
                    onDoubleClick = { component.onDoubleClick(it) },
                    onContextMenuClick = { menu, _ ->
                        handleContextMenuClick(
                            component = component,
                            scope = scope,
                            deleteDialog = deleteDialog,
                            menu = menu,
                            path = path
                        )
                    }
                )
            }
        }
    }
}

private fun handleContextMenuClick(
    component: FileManagerComponent,
    scope: CoroutineScope,
    deleteDialog: ConfirmationDialogController,
    menu: ContextMenu,
    path: Path
) {

    scope.launch {
        when (menu.menuType) {
            ContextMenuType.Delete -> {
                val deleteWhat =
                    if (path.toFile().isDirectory) "Folder" else "File"
                deleteDialog.show(
                    title = "Delete $deleteWhat",
                    message = "Are you sure you want to ${path.name} $deleteWhat",
                    menu,
                    path
                )
            }

            ContextMenuType.CopyToInternal -> copyToInternal(path)
            else -> component.onContextMenuClick(menu, path)
        }
    }
}

private suspend fun copyToInternal(path: Path) {
    val interCommunication = getKoinInstance<InterCommunication>()
    interCommunication.pathReceived.emit(path)
}
