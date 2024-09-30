package org.smartregister.fct.fhirman.presentation.components

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import org.smartregister.fct.common.presentation.component.ScreenComponent
import org.smartregister.fct.common.util.windowTitle
import org.smartregister.fct.engine.util.componentScope

class FhirmanScreenComponent(
    componentContext: ComponentContext
) : ScreenComponent, ComponentContext by componentContext {

    init {
        componentScope.launch {
            windowTitle.emit("Fhirman")
        }
    }
}