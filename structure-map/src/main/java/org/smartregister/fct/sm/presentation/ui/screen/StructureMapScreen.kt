package org.smartregister.fct.sm.presentation.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.delay
import org.smartregister.fct.aurora.presentation.ui.components.CloseableTab
import org.smartregister.fct.aurora.presentation.ui.components.TabRow
import org.smartregister.fct.sm.presentation.component.StructureMapScreenComponent
import org.smartregister.fct.sm.presentation.ui.components.CreateNewSMButton
import org.smartregister.fct.sm.presentation.ui.components.StructureMapTabItem
import org.smartregister.fct.sm.presentation.ui.components.dialog.DeleteStructureMapConfirmationDialog

@Composable
fun StructureMapScreen(component: StructureMapScreenComponent) {

    val smDetailList by component.smTabComponents.subscribeAsState()
    val activeTabIndex by component.activeTabIndex.subscribeAsState()

    Column {

        if (smDetailList.isNotEmpty()) {

            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = activeTabIndex,
            ) {
                smDetailList
                    .map { it.smDetail }
                    .forEachIndexed { index, item ->
                        CloseableTab(
                            index = index,
                            item = item,
                            title = { it.title },
                            selected = index == activeTabIndex,
                            onClick = {
                                component.changeTab(it)
                            },
                            onClose = {
                                component.showDeleteStructureMapDialog(it)
                            }
                        )
                    }
            }

            Scaffold(
                floatingActionButton = {
                    CreateNewSMButton(
                        label = null,
                        icon = Icons.Outlined.Add,
                        component = component
                    )
                }
            ) {
                Box(Modifier.padding(it)) {
                    StructureMapTabItem(smDetailList[activeTabIndex])
                }
            }
        } else {
            CreateNewStructureMap(component = component)
        }
    }

    DeleteStructureMapConfirmationDialog(component)
}

@Composable
private fun CreateNewStructureMap(component: StructureMapScreenComponent) {
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
                modifier = Modifier.align(Alignment.Center),
                component = component
            )
        }
    }

    LaunchedEffect(showButton) {
        delay(300)
        showButton = true
    }
}