package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.fm.ui.viewmodel.InAppFileManagerViewModel

@Composable
fun InAppFileManager(
    mode: FileManagerMode = FileManagerMode.Edit
) {

    val viewModel: InAppFileManagerViewModel = koinInject()
    viewModel.setMode(mode)

    val scope = rememberCoroutineScope()
    val activePath by viewModel.getActivePath().collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (mode is FileManagerMode.Edit) Title("App File Manager")
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

            ConstraintLayout {
                val (contentRef, pathRef) = createRefs()

                Content(
                    pathRef = pathRef,
                    contentRef = contentRef,
                    viewModel = viewModel
                ) {
                    DefaultContentOptions(viewModel)
                }

                Breadcrumb(pathRef, activePath)
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {
            CreateNewFolder(viewModel)
        }
    }
}

