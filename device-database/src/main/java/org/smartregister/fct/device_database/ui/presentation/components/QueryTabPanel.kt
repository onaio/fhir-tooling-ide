package org.smartregister.fct.device_database.ui.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.smartregister.fct.common.domain.model.ResizeOption
import org.smartregister.fct.common.presentation.ui.components.VerticalSplitPane
import org.smartregister.fct.device_database.ui.components.QueryTabComponent

@Composable
fun QueryTabPanel(tabComponent: QueryTabComponent) {
    val resultComponent by tabComponent.queryResultDTController.collectAsState()

    Column {
        QueryToolbar(tabComponent)
        HorizontalDivider()
        VerticalSplitPane(
            resizeOption = ResizeOption.Flexible(
                savedKey = tabComponent,
                sizeRatio = 0.3f,
                minSizeRatio = 0.1f,
                maxSizeRatio = 0.9f
            ),
            topContent = {
                QueryEditor(tabComponent)
            },
            bottomContent = {
                if (resultComponent != null) {
                    QueryResult(
                        resultComponent!!,
                        componentContext = tabComponent
                    )
                }
            },
            enableBottomContent = resultComponent != null
        )
    }
}