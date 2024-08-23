package org.smartregister.fct.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.smartregister.fct.configs.ui.ConfigManagerScreen
import org.smartregister.fct.common.presentation.component.RootComponent
import org.smartregister.fct.fhirman.presentation.components.FhirmanScreenComponent
import org.smartregister.fct.fhirman.presentation.ui.screen.Fhirman
import org.smartregister.fct.fm.presentation.ui.screen.FileManagerScreen
import org.smartregister.fct.presentation.component.DataSpecificationScreenComponent
import org.smartregister.fct.fm.presentation.components.FileManagerScreenComponent
import org.smartregister.fct.sm.presentation.component.StructureMapScreenComponent
import org.smartregister.fct.sm.presentation.ui.screen.StructureMapScreen

@Composable
fun MainRoot(component: RootComponent) {

    val mainSlot by component.slot.subscribeAsState()

    when (val contextComponent = mainSlot.child?.instance) {
        is DataSpecificationScreenComponent -> ConfigManagerScreen()
        is StructureMapScreenComponent -> StructureMapScreen(contextComponent)
        is FileManagerScreenComponent -> FileManagerScreen(contextComponent)
        is FhirmanScreenComponent -> Fhirman(contextComponent)
    }
}