package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import okio.Path
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
    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))) {
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

                    }
                )
            }
        }

        VerticalDivider(Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
private fun Content(viewModel: SystemFileManagerViewModel, activeDir: Path) {

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