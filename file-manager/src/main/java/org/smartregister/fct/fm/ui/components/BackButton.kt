package org.smartregister.fct.fm.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.smartregister.fct.fm.ui.viewmodel.FileManagerViewModel
import org.smartregister.fct.aurora.ui.components.SmallIconButton

@Composable
internal fun BackButton(
    modifier: Modifier,
    viewModel: FileManagerViewModel
) {
    val scope = rememberCoroutineScope()
    val activePath by viewModel.getActivePath().collectAsState()

    SmallIconButton(
        modifier = modifier,
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
}