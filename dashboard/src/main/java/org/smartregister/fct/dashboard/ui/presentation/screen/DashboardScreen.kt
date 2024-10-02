package org.smartregister.fct.dashboard.ui.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import org.smartregister.fct.dashboard.ui.components.DashboardScreenComponent
import org.smartregister.fct.dashboard.ui.presentation.components.DeviceInfo
import org.smartregister.fct.dashboard.ui.presentation.components.FhirResourcesList
import org.smartregister.fct.dashboard.ui.presentation.components.Widget

@Composable
fun DashboardScreen(component: DashboardScreenComponent) {
    ConstraintLayout(Modifier.fillMaxSize()) {

        val (deviceInfoRef, resourceListRef) = createRefs()

        Widget(
            modifier = Modifier.width(330.dp).constrainAs(deviceInfoRef) {
                top.linkTo(parent.top, 16.dp)
                start.linkTo(parent.start, 16.dp)
                //bottom.linkTo(parent.bottom, 16.dp)
                width = Dimension.preferredWrapContent
                //height = Dimension.fillToConstraints
            }
        ) {
            DeviceInfo(component.deviceInfoComponent)
        }

        Widget(
            modifier = Modifier.width(330.dp).constrainAs(resourceListRef) {
                top.linkTo(deviceInfoRef.bottom, 16.dp)
                start.linkTo(parent.start, 16.dp)
                bottom.linkTo(parent.bottom, 16.dp)
                width = Dimension.preferredWrapContent
                height = Dimension.fillToConstraints
            }
        ) {
            FhirResourcesList()
        }
    }
}