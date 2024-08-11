package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import okio.Path
import org.smartregister.fct.engine.data.helper.AppSettingProvide.getKoin
import org.smartregister.fct.fm.ui.viewmodel.SystemFileManagerViewModel
import org.smartregister.fct.radiance.ui.components.LabelledCheckBox
import org.smartregister.fct.radiance.ui.components.SmallIconButton

@Composable
fun SystemFileManager(viewModel: SystemFileManagerViewModel = getKoin().get<SystemFileManagerViewModel>()) {

    val activeDir by viewModel.getActivePath().collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Title("System File Manager")
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonNavigation(viewModel, activeDir)
            Content(viewModel, activeDir)
        }
    }
}

@Composable
private fun CommonNavigation(viewModel: SystemFileManagerViewModel, activePath: Path) {
    val scope = rememberCoroutineScope()

    Box(
        Modifier.width(150.dp).fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceContainer).alpha(0.8f)
    ) {
        Column(Modifier.padding(top = 4.dp)) {

            viewModel.getCommonDirs().forEach {
                NavigationItem(
                    directory = it,
                    selected = it.path.toString() == activePath.toString(),
                    onClick = { selectedDir ->
                        scope.launch {
                            viewModel.setActivePath(selectedDir.path)
                        }
                    }
                )
            }

            viewModel.getRootDirs().takeIf { it.isNotEmpty() }?.let { dirs ->
                Spacer(Modifier.height(4.dp))
                HorizontalDivider(Modifier)
                Spacer(Modifier.height(4.dp))
                dirs.forEach {
                    NavigationItem(
                        directory = it,
                        selected = it.path.toString() == activePath.toString(),
                        onClick = {  selectedDir ->
                            scope.launch {
                                viewModel.setActivePath(selectedDir.path)
                            }
                        }
                    )
                }
            }
        }

        VerticalDivider(Modifier.align(Alignment.CenterEnd))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Content(viewModel: SystemFileManagerViewModel, activePath: Path) {
    val activePathContent by viewModel.getActivePathContent().collectAsState()
    val verticalScrollState = rememberScrollState()
    Column(Modifier.fillMaxSize()) {
        ContentOptions(viewModel, activePath)
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(verticalScrollState),
            ) {
                Spacer(Modifier.height(12.dp))
                FlowRow(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    activePathContent.forEach { path ->
                        ContentItem(path, viewModel)
                    }
                }
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

@Composable
private fun ContentOptions(viewModel: SystemFileManagerViewModel, activePath: Path) {
    val scope = rememberCoroutineScope()
    Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SmallIconButton(
                modifier = Modifier,
                icon = Icons.AutoMirrored.Outlined.ArrowBack,
                enable = activePath.toString() !in viewModel.getCommonDirs().map { it.path.toString() },
                onClick = {
                    scope.launch {
                        activePath.parent?.let {
                            viewModel.setActivePath(it)
                        }
                    }
                }
            )

            val showHiddenFile by viewModel.getShowHiddenFile().collectAsState()

            LabelledCheckBox(
                checked = showHiddenFile,
                label = "Show Hidden Files",
                onCheckedChange = {
                    scope.launch {
                        viewModel.setShowHiddenFile(it)
                    }
                }
            )
        }

        HorizontalDivider(Modifier.align(Alignment.BottomCenter))
    }
}