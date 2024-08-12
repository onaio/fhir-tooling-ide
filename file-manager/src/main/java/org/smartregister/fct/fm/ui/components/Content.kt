package org.smartregister.fct.fm.ui.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import okio.Path
import org.smartregister.fct.fm.ui.viewmodel.FileManagerViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Content(
    viewModel: FileManagerViewModel,
    contentOption: @Composable ColumnScope.() -> Unit,
) {
    val activePathContent by viewModel.getActivePathContent().collectAsState()
    val verticalScrollState = rememberScrollState()
    Column(Modifier.fillMaxSize()) {
        contentOption(this)
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