package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import org.smartregister.fct.engine.data.helper.AppSettingProvide.getKoin
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.fm.ui.viewmodel.InAppFileManagerViewModel
import org.smartregister.fct.fm.ui.viewmodel.SystemFileManagerViewModel

@Composable
fun SystemFileManager(
    mode: FileManagerMode = FileManagerMode.Edit
) {

    val viewModel: SystemFileManagerViewModel = koinInject<SystemFileManagerViewModel> { parametersOf(mode) }
    viewModel.setMode(mode)

    val scope = rememberCoroutineScope()
    val activePath by viewModel.getActivePath().collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (mode is FileManagerMode.Edit) Title("System File Manager")
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

            ConstraintLayout {
                val (contentRef, pathRef) = createRefs()

                Content(
                    pathRef = pathRef,
                    contentRef = contentRef,
                    viewModel = viewModel,
                ) {
                    ContentOptions(viewModel)
                }

                Breadcrumb(pathRef, activePath)
            }


        }
    }
}