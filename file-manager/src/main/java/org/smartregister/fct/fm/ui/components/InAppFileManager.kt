package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.smartregister.fct.engine.data.helper.AppSettingProvide.getKoin
import org.smartregister.fct.fm.ui.viewmodel.InAppFileManagerViewModel
import org.smartregister.fct.aurora.ui.components.dialog.DialogType
import org.smartregister.fct.aurora.ui.components.dialog.rememberAlertDialog
import org.smartregister.fct.aurora.ui.components.dialog.rememberSingleFieldDialog
import org.smartregister.fct.aurora.util.newFolderNameValidation

@Composable
fun InAppFileManager(viewModel: InAppFileManagerViewModel = getKoin().get()) {

    val activePath by viewModel.getActivePath().collectAsState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Title("App File Manager")
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonNavigation(
                activePath = activePath,
                commonDirs = viewModel.getCommonDirs(),
                onDirectoryClick = { activePath ->
                    scope.launch {
                        viewModel.setActivePath(activePath)
                    }
                },
            )
            Content(viewModel) {
                DefaultContentOptions(viewModel)
            }
        }
    }

}


@Composable
private fun DefaultContentOptions(viewModel: InAppFileManagerViewModel) {

    ContentOptions(
        viewModel = viewModel
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            CreateNewFolder(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateNewFolder(viewModel: InAppFileManagerViewModel) {

    val alertDialog = rememberAlertDialog("Error") {
        Text(it.getExtra<String>() ?: "Error on creating new folder")
    }

    val newFolderDialog = rememberSingleFieldDialog(
        title = "Create New Folder",
        validations = listOf(newFolderNameValidation)
    ) { folderName, _ ->
        val result = viewModel.createNewFolder(folderName)
        if (result.isFailure) {
            alertDialog.show(result.exceptionOrNull()?.message, DialogType.Error)
        }
    }

    Chip(
        modifier = Modifier.height(30.dp),
        colors = ChipDefaults.chipColors(
            backgroundColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            newFolderDialog.show()
        },
    ) {
        Text(
            text = "New Folder",
            style = MaterialTheme.typography.bodySmall
        )
    }
}