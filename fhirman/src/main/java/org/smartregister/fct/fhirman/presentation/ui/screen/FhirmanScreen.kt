package org.smartregister.fct.fhirman.presentation.ui.screen

import androidx.compose.runtime.Composable
import org.smartregister.fct.common.presentation.ui.container.Aurora
import org.smartregister.fct.common.domain.model.ResizeOption
import org.smartregister.fct.common.presentation.ui.components.HorizontalSplitPane
import org.smartregister.fct.fhirman.presentation.components.FhirmanScreenComponent
import org.smartregister.fct.fhirman.presentation.ui.components.FhirmanServerPanel

@Composable
fun Fhirman(component: FhirmanScreenComponent) {

    HorizontalSplitPane(
        resizeOption = ResizeOption.Flexible(
            minSizeRatio = 0.2f,
            maxSizeRatio = 0.8f
        ),
        leftContent = {

        },
        rightContent = {
            with(component) {
                Aurora(
                    componentContext = component
                ) {
                    FhirmanServerPanel(this)
                }
            }
        }
    )

}