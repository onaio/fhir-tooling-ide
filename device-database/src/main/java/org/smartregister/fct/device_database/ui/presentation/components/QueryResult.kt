package org.smartregister.fct.device_database.ui.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.datatable.presentation.ui.view.DataTable
import org.smartregister.fct.device_database.data.controller.QueryResultDataController
import org.smartregister.fct.device_database.domain.model.QueryMethod

@Composable
internal fun QueryResult(component: QueryResultDataController, componentContext: ComponentContext) {

    val queryResponse = component.queryResponse
    CheckQueryResponseError(queryResponse)

    if (queryResponse.queryMethod == QueryMethod.execSql) {
        if (queryResponse.error == null) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = "Query successfully executed",
            )
        }
    } else {
        if (queryResponse.error == null) {
            DataTable(
                controller = component,
                componentContext = componentContext,
            )
        }
    }

}