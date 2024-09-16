package org.smartregister.fct.device_database.ui.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.smartregister.fct.aurora.presentation.ui.components.LinearIndicator
import org.smartregister.fct.datatable.presentation.ui.view.DataTable
import org.smartregister.fct.device_database.ui.components.TableTabComponent

@Composable
internal fun TableTabPanel(tableTabComponent: TableTabComponent) {

    val isLoading by tableTabComponent.loading.collectAsState()
    val tableRequestDataController by tableTabComponent.tableResultDataController.collectAsState()

    Column {
        if (isLoading) {
            LinearIndicator()
        }

        if (tableRequestDataController != null) {

            val queryResponse = tableRequestDataController!!.queryResponse

            CheckQueryResponseError(
                queryResponse = queryResponse,
                reloadAction = {
                    tableTabComponent.runQuery()
                }
            )
            if (queryResponse.error == null) {
                DataTable(
                    controller = tableRequestDataController!!,
                    componentContext = tableTabComponent
                )
            }
        }
    }
}