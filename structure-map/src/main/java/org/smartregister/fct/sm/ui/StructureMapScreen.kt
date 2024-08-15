package org.smartregister.fct.sm.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Scaffold
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
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin
import org.smartregister.fct.aurora.ui.components.ScreenContainer
import org.smartregister.fct.aurora.ui.components.SmallFloatingActionIconButton
import org.smartregister.fct.aurora.ui.components.Tabs
import org.smartregister.fct.aurora.ui.components.dialog.rememberConfirmationDialog
import org.smartregister.fct.editor.data.enums.FileType
import org.smartregister.fct.editor.ui.CodeEditor
import org.smartregister.fct.engine.data.locals.LocalSnackbarHost
import org.smartregister.fct.sm.data.viewmodel.SMViewModel
import org.smartregister.fct.sm.domain.model.SMDetail
import org.smartregister.fct.sm.ui.components.CreateNewSMButton
import org.smartregister.fct.sm.ui.components.SMOptionWindow

class StructureMapScreen : Screen {
    @Composable
    override fun Content() {

        val snackbarHostState = LocalSnackbarHost.current
        val scope = rememberCoroutineScope()
        val viewModel = getKoin().get<SMViewModel>()
        val smDetailList by viewModel.getAllSMList().collectAsState(initial = listOf())

        LaunchedEffect(smDetailList.size) {

            viewModel.clearActiveSMTabViewModel()
            smDetailList.forEachIndexed { index, smDetail ->
                launch { viewModel.addSMTabViewModel(smDetail) }

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

        ScreenContainer(
            leftWindow = { SMOptionWindow() }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Tabs(
                    tabs = smDetailList,
                    title = { it.title },
                    onClose = {
                        smDeleteConfirmDialogController.show(it)
                    },
                    onSelected = { _, smDetail ->
                        scope.launch {
                            viewModel.updateActiveSMTabViewModel(smDetail.id)
                        }
                    }
                )

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
                                    controller = smTabViewModel.codeController
                                )
                            }
                        }
                    } ?: CreateNewStructureMap()
            }
        }

    }
}

@Composable
private fun CreateNewStructureMap() {
    var showButton by remember { mutableStateOf(false) }

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = showButton,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            CreateNewSMButton(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    LaunchedEffect(showButton) {
        delay(300)
        showButton = true
    }
}