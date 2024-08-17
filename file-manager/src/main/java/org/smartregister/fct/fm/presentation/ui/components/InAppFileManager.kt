package org.smartregister.fct.fm.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import org.smartregister.fct.fm.domain.datasource.FileSystem
import org.smartregister.fct.fm.domain.model.FileManagerMode
import org.smartregister.fct.fm.presentation.components.InAppFileManagerComponent

@Composable
fun InAppFileManager(
    componentContext: ComponentContext,
    mode: FileManagerMode = FileManagerMode.Edit
) {

    val fileSystem: FileSystem = koinInject(qualifier = named("inApp"))
    val component = remember {
        InAppFileManagerComponent(
            componentContext = componentContext,
            fileSystem = fileSystem,
            mode = mode
        )
    }

    val scope = rememberCoroutineScope()
    val activePath by component.getActivePath().collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (mode is FileManagerMode.Edit) Title("App File Manager")
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonNavigation(
                activePath = activePath,
                commonDirs = component.getCommonDirs(),
                onDirectoryClick = { activePath ->
                    scope.launch {
                        component.setActivePath(activePath)
                    }
                },
            )

            ConstraintLayout {
                val (contentRef, pathRef) = createRefs()

                Content(
                    pathRef = pathRef,
                    contentRef = contentRef,
                    component = component
                ) {
                    DefaultContentOptions(component)
                }

                Breadcrumb(pathRef, activePath)
            }

        }
    }

}


@Composable
private fun DefaultContentOptions(component: InAppFileManagerComponent) {

    ContentOptions(
        component = component
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {
            CreateNewFolder(component)
        }
    }
}

