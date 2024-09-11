package org.smartregister.fct.fhirman.presentation.ui.screen

import androidx.compose.runtime.Composable
import org.smartregister.fct.common.presentation.ui.container.Aurora
import org.smartregister.fct.fhirman.presentation.components.FhirmanScreenComponent
import org.smartregister.fct.fhirman.presentation.ui.components.FhirmanServerPanel

@Composable
fun FhirmanScreen(component: FhirmanScreenComponent) {
    with(component) {
        Aurora(
            componentContext = component
        ) {
            FhirmanServerPanel(this)
        }
    }

}