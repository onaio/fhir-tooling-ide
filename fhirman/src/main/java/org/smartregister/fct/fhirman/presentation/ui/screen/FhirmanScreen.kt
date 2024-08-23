@file:Suppress("UNUSED", "INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package org.smartregister.fct.fhirman.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import org.smartregister.fct.aurora.presentation.ui.components.AutoCompleteDropDown
import org.smartregister.fct.aurora.presentation.ui.components.Button
import org.smartregister.fct.aurora.presentation.ui.components.HorizontalButtonStrip
import org.smartregister.fct.aurora.presentation.ui.components.OutlinedTextField
import org.smartregister.fct.aurora.util.pxToDp
import org.smartregister.fct.common.domain.model.ResizeOption
import org.smartregister.fct.common.presentation.ui.components.ScreenContainer
import org.smartregister.fct.common.presentation.ui.components.VerticalSplitPane
import org.smartregister.fct.common.util.listOfAllFhirResources
import org.smartregister.fct.fhirman.presentation.components.FhirmanScreenComponent
import org.smartregister.fct.fhirman.presentation.components.FhirmanServerComponent
import org.smartregister.fct.fhirman.presentation.ui.components.ConfigPanel
import org.smartregister.fct.fhirman.presentation.ui.components.NoConfig

@Composable
fun Fhirman(component: FhirmanScreenComponent) {


    VerticalSplitPane(
        resizeOption = ResizeOption.Flexible(
            minSizeRatio = 0.2f,
            maxSizeRatio = 0.8f
        ),
        leftContent = {

        },
        rightContent = {
            with(component) {
                FhirmanServerPanel()
            }
        }
    )

}

@Composable
fun ComponentContext.FhirmanServerPanel() {

    val fhirmanServerComponent = remember { FhirmanServerComponent(this) }

    with(fhirmanServerComponent) {
        val configs by configs.subscribeAsState()

        if (configs.isNotEmpty()) {
            ScreenContainer(
                panelWidth = 180.dp,
                leftPanel = {
                    ConfigPanel(configs)
                },
                mainContent = {
                    MainContent()
                }
            )

        } else {
            NoConfig()
        }
    }
}


@Composable
internal fun FhirmanServerComponent.MainContent() {

    Column {
        val options = listOf("GET", "POST", "PUT", "DELETE")

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(12.dp),
        ) {
            HorizontalButtonStrip(
                options = options,
                label = { it },
                isExpanded = true,
                stripBackgroundColor = MaterialTheme.colorScheme.background,
                onClick = {

                }
            )
        }

        HorizontalDivider()

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {

            val (resType, resId, btnSend) = createRefs()

            Box(modifier = Modifier
                .width(180.dp).constrainAs(resType) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    //width = Dimension.matchParent
                }) {

                AutoCompleteDropDown(
                    modifier = Modifier.fillMaxWidth(),
                    items = listOfAllFhirResources,
                    label = { it },
                    heading = "Resource",
                    onTextChanged = { text, isMatch ->
                        println("$text - $isMatch")
                    }
                )
            }

            var resIdText by remember { mutableStateOf("") }

            OutlinedTextField(
                modifier = Modifier.constrainAs(resId) {
                    start.linkTo(resType.end, margin = 8.dp)
                    top.linkTo(parent.top)
                    end.linkTo(btnSend.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
                value = resIdText,
                onValueChange = {
                    resIdText = it
                },
                label = "Id"
            )

            Button(
                modifier = Modifier.constrainAs(btnSend) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                label = "SEND",
                onClick = {}
            )
        }
    }
}