package org.smartregister.fct.fm.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import org.smartregister.fct.aurora.ui.components.dialog.DialogType
import org.smartregister.fct.aurora.ui.components.dialog.getOrDefault
import org.smartregister.fct.aurora.ui.components.dialog.rememberAlertDialog
import org.smartregister.fct.fm.ui.viewmodel.FileManagerViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ConstraintLayoutScope.Content(
    pathRef: ConstrainedLayoutReference,
    contentRef: ConstrainedLayoutReference,
    viewModel: FileManagerViewModel,
    contentOption: @Composable ColumnScope.() -> Unit,
) {
    val activePathContent by viewModel.getActivePathContent().collectAsState()
    val verticalScrollState = rememberScrollState()
    val visible by viewModel.visibleItem.collectAsState()

    val copyErrorDialog = rememberAlertDialog(
        title = "Error",
        dialogType = DialogType.Error
    ) { it ->
        val error = it.getExtra().getOrDefault<Exception?, String>(0, "Unknown Error") { ex, default ->
            ex?.message ?: default
        }
        Text(error)
    }

    Column(
        modifier = Modifier.constrainAs(contentRef) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
            bottom.linkTo(pathRef.top)
            height = Dimension.preferredWrapContent
        }
    ) {
        contentOption(this)
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(verticalScrollState),
            ) {
                Spacer(Modifier.height(12.dp))
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        activePathContent.forEach { path ->
                            ContentItem(path, viewModel, copyErrorDialog)
                        }
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