package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.smartregister.fct.engine.data.helper.AppSettingProvide.getKoin
import org.smartregister.fct.fm.ui.viewmodel.SystemFileManagerViewModel
import org.smartregister.fct.aurora.ui.components.LabelledCheckBox

@Composable
fun SystemFileManager(viewModel: SystemFileManagerViewModel = getKoin().get()) {

    val activePath by viewModel.getActivePath().collectAsState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Title("System File Manager")
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonNavigation(
                activePath = activePath,
                commonDirs = viewModel.getCommonDirs(),
                rootDirs = viewModel.getRootDirs(),
                onDirectoryClick = { activePath ->
                    scope.launch {
                        viewModel.setActivePath(activePath)
                    }
                },
            )
            Content(
                viewModel = viewModel,
            ) {
                DefaultContentOptions(viewModel)
            }
        }
    }
}

@Composable
private fun DefaultContentOptions(
    viewModel: SystemFileManagerViewModel
) {
    val scope = rememberCoroutineScope()
    ContentOptions(
        viewModel = viewModel
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            val showHiddenFile by viewModel.getShowHiddenFile().collectAsState()

            LabelledCheckBox(
                checked = showHiddenFile,
                label = "Hidden Files",
                onCheckedChange = {
                    scope.launch {
                        viewModel.setShowHiddenFile(it)
                    }
                }
            )
        }
    }
}