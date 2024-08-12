package org.smartregister.fct.fm.ui.components

import androidx.compose.foundation.clickable
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
import org.smartregister.fct.fm.ui.viewmodel.FileManagerViewModel
import org.smartregister.fct.fm.util.getFileTypeImage
import org.smartregister.fct.aurora.ui.components.Icon

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ContentItem(path: Path, viewModel: FileManagerViewModel) {
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
                    .clickable { }.onPointerEvent(
                        PointerEventType.Press
                    ) {
                        when {
                            it.buttons.isPrimaryPressed -> when (it.awtEventOrNull?.clickCount) {
                                2 -> {
                                    scope.launch {
                                        delay(200)
                                        viewModel.setActivePath(path)
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