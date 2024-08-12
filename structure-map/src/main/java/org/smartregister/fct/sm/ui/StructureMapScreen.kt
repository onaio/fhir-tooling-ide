package org.smartregister.fct.sm.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import org.smartregister.fct.editor.data.enums.CodeStyle
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.engine.data.locals.LocalSnackbarHost
import org.smartregister.fct.aurora.ui.components.SmallFloatingActionIconButton
import org.smartregister.fct.aurora.ui.components.Tab
import org.smartregister.fct.aurora.ui.components.TabRow
import org.smartregister.fct.aurora.ui.components.dialog.rememberConfirmationDialog
import org.smartregister.fct.sm.data.viewmodel.SMViewModel
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.ui.components.UploadSMButton

class StructureMapScreen : Screen {

    @Composable
    override fun Content() {

        val snackbarHostState = LocalSnackbarHost.current
        val scope = rememberCoroutineScope()
        val viewModel = getKoin().get<SMViewModel>()
        var tabIndex by remember { mutableStateOf(0) }
        val smDetailList by viewModel.getAllSMList().collectAsState(initial = listOf())

        LaunchedEffect(smDetailList.size) {

            smDetailList.forEachIndexed { index, smDetail ->
                viewModel.addSMTabViewModel(smDetail)

                if (index == 0) {
                    viewModel.updateActiveSMTabViewModel(smDetail.id)
                }
            }
        }

        val smDeleteConfirmDialogController = rememberConfirmationDialog<SMDetail>(
            title = "Delete Structure Map",
            message = "Are you sure you want to delete this structure-map?"
        ) { smDetail ->
            smDetail?.run {
                viewModel.delete(smDetail.id)
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {

            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = tabIndex,
            ) {
                smDetailList.forEachIndexed { index, smDetail ->

                    Tab(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    smDetail.title,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Box(
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .clickable(
                                            onClick = {
                                                smDeleteConfirmDialogController.show(
                                                    smDetail
                                                )
                                            },
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(
                                                bounded = false,
                                                radius = 15.dp
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = Icons.Rounded.Close,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        selected = index == tabIndex,
                        onClick = {
                            scope.launch {
                                viewModel.updateActiveSMTabViewModel(smDetail.id)
                                tabIndex = index
                            }
                        }
                    )
                }
            }

            val activeSMTabViewModel by viewModel.getActiveSMTabViewModel()
                .collectAsState()

            activeSMTabViewModel
                ?.let { smTabViewModel ->
                    Scaffold(
                        floatingActionButton = {
                            SmallFloatingActionIconButton(
                                icon = Icons.Outlined.Save,
                                onClick = {
                                    scope.launch {
                                        viewModel.update(
                                            smTabViewModel.smDetail.copy(
                                                body = smTabViewModel.codeController.getText()
                                            )
                                        )
                                        snackbarHostState.showSnackbar("${smTabViewModel.smDetail.title} has been updated.")
                                    }
                                }
                            )
                        }
                    ) {
                        Box(Modifier.padding(it)) {
                            CodeEditor(
                                codeStyle = CodeStyle.StructureMap,
                                controller = smTabViewModel.codeController
                            )
                        }
                    }
                } ?: UploadSMButton()
        }

    }
}