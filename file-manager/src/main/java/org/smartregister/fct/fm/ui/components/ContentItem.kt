package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okio.Path
import org.koin.compose.getKoin
import org.koin.core.Koin
import org.smartregister.fct.aurora.domain.controller.DialogController
import org.smartregister.fct.aurora.ui.components.Icon
import org.smartregister.fct.aurora.ui.components.dialog.DialogType
import org.smartregister.fct.aurora.ui.components.dialog.getOrDefault
import org.smartregister.fct.aurora.ui.components.dialog.rememberAlertDialog
import org.smartregister.fct.aurora.ui.components.dialog.rememberConfirmationDialog
import org.smartregister.fct.fm.data.FileHandler
import org.smartregister.fct.fm.domain.model.Applicable
import org.smartregister.fct.fm.domain.model.ContextMenu
import org.smartregister.fct.fm.domain.model.ContextMenuType
import org.smartregister.fct.fm.ui.viewmodel.FileManagerViewModel
import org.smartregister.fct.fm.util.getFileTypeImage

@Composable
internal fun ContentItem(
    path: Path,
    viewModel: FileManagerViewModel,
    copyErrorDialog: DialogController
) {

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ContextMenu(path, viewModel, copyErrorDialog) {
            ContentIcon(path, viewModel)
        }

        Text(
            modifier = Modifier.width(95.dp)
                .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 10.dp),
            text = path.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun ContentIcon(path: Path, viewModel: FileManagerViewModel) {
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier.width(84.dp).padding(horizontal = 12.dp)
            .onDrag { },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Box(
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .clickable { }.onPointerEvent(
                    PointerEventType.Press
                ) {
                    when {
                        it.buttons.isPrimaryPressed -> when (it.awtEventOrNull?.clickCount) {
                            2 -> {
                                scope.launch {
                                    delay(200)
                                    viewModel.onDoubleClick(path)
                                }
                            }
                        }
                    }
                },
        ) {
            Icon(
                modifier = Modifier.size(50.dp).align(Alignment.Center),
                icon = path.getFileTypeImage(),
            )
        }
    }
}

@Composable
private fun ContextMenu(
    path: Path,
    viewModel: FileManagerViewModel,
    copyErrorDialog: DialogController,
    content: @Composable () -> Unit
) {
    val koin = getKoin()
    val scope = rememberCoroutineScope()
    val deleteWhat = if (path.toFile().isDirectory) "Folder" else "File"

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

    val deleteDialog = rememberConfirmationDialog<Pair<ContextMenu, Path>>(
        title = "Delete $deleteWhat",
        message = "Are you sure you want to ${path.name} $deleteWhat"
    ) {
        val result = viewModel.onContextMenuClick(it!!.first, it.second)
        if (result.isFailure) {
            deleteErrorDialog.show(result.exceptionOrNull())
        }
    }

    ContextMenuArea(
        items = {
            viewModel
                .getContextMenuList()
                .filter {
                    !path.toFile().isHidden && it.applicable is Applicable.Both ||
                            (path.toFile().isFile && it.applicable is Applicable.File) ||
                            (path.toFile().isDirectory && it.applicable is Applicable.Folder)
                }
                .map {
                    ContextMenuItem(it.menuType.label) {
                        scope.launch {
                            when (it.menuType) {
                                ContextMenuType.Delete -> deleteDialog.show(Pair(it, path))
                                ContextMenuType.CopyToInternal -> copyToInternal(
                                    koin,
                                    path,
                                    copyErrorDialog
                                )

                                else -> viewModel.onContextMenuClick(it, path)
                            }
                        }
                    }
                }
        },
        content = content
    )
}

private suspend fun copyToInternal(koin: Koin, path: Path, copyErrorDialog: DialogController) {
    val result = koin.get<FileHandler>().inAppFileHandler.copy(path)
    if (result.isFailure) {
        copyErrorDialog.show(
            result.exceptionOrNull(),
            DialogType.Error
        )
    }
}