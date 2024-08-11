package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cheonjaeung.compose.grid.SimpleGridCells
import com.cheonjaeung.compose.grid.VerticalGrid
import fct.file_manager.generated.resources.Res
import fct.file_manager.generated.resources.file
import fct.file_manager.generated.resources.folder
import kotlinx.coroutines.launch
import okio.Path
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.smartregister.fct.engine.data.helper.AppSettingProvide.getKoin
import org.smartregister.fct.fm.domain.model.Directory
import org.smartregister.fct.fm.ui.viewmodel.SystemFileManagerViewModel
import org.smartregister.fct.radiance.ui.components.TextButton

@Composable
fun SystemFileManager() {

    val viewModel = getKoin().get<SystemFileManagerViewModel>()
    val activeDir by viewModel.getActiveDir().collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Title()
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            CommonNavigation(viewModel, activeDir)
            Content(viewModel, activeDir)
        }
    }
}

@Composable
fun Title() {
    Box(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
    ) {
        Text(
            modifier = Modifier.padding(8.dp).align(Alignment.Center),
            text = "System File Manager",
            style = MaterialTheme.typography.titleSmall
        )

        HorizontalDivider(Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun CommonNavigation(viewModel: SystemFileManagerViewModel, activeDir: Path) {
    val scope = rememberCoroutineScope()

    Box(
        Modifier.width(150.dp).fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceContainer).alpha(0.8f)
            .padding(top = 4.dp)
    ) {
        Column {

            viewModel.getCommonDirs().forEach {
                NavigationItem(
                    directory = it,
                    selected = it.path.toString() == activeDir.toString(),
                    onClick = { selectedDir ->
                        scope.launch {
                            viewModel.setActiveDir(selectedDir.path)
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
                        selected = it.path.toString() == activeDir.toString(),
                        onClick = {  selectedDir ->
                            scope.launch {
                                viewModel.setActiveDir(selectedDir.path)
                            }
                        }
                    )
                }
            }
        }

        VerticalDivider(Modifier.align(Alignment.CenterEnd))
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun Content(viewModel: SystemFileManagerViewModel, activeDir: Path) {
    val activeDirContent by viewModel.getActiveDirContent().collectAsState()
    val verticalScrollState = rememberScrollState()

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
                activeDirContent.forEach { path ->
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ContentItem(path: Path, viewModel: SystemFileManagerViewModel) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.width(84.dp).padding(horizontal = 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .clickable {  }.onPointerEvent(
                        PointerEventType.Press) {
                        when {
                            it.buttons.isPrimaryPressed -> when (it.awtEventOrNull?.clickCount) {
                                2 -> {
                                    scope.launch {
                                        viewModel.setActiveDir(path)
                                    }
                                }
                            }
                        }
                    },
            ) {
                Image(
                    modifier = Modifier.size(60.dp).align(Alignment.Center),
                    painter = painterResource(path.getFileTypeImage()),
                    contentDescription = null
                )
            }
        }


        Text(
            modifier = Modifier.width(95.dp).padding(horizontal = 12.dp),
            text = path.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
internal fun NavigationItem(directory: Directory, selected: Boolean, onClick: (Directory) -> Unit) {

    /*Row(
        modifier = Modifier.fillMaxWidth().clickable {

        }.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        directory.icon?.let { icon ->
            Icon(icon = icon)
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = directory.name
        )
    }*/

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
            modifier = Modifier.fillMaxWidth(0.9f),
            label = directory.name,
            icon = directory.icon,
            selected = selected,
            textAlign = TextAlign.Start,
            onClick = {
                onClick(directory)
            }
        )
    }
}

fun Path.getFileTypeImage() : DrawableResource {
    return when {
        toFile().isDirectory -> Res.drawable.folder
        else -> Res.drawable.file
    }
}