package org.smartregister.fct.device_database.ui.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.json.JSONObject
import org.smartregister.fct.common.presentation.ui.components.datatable.DataTable
import org.smartregister.fct.device_database.domain.model.QueryResponse
import org.smartregister.fct.device_database.ui.components.QueryResultDTController

@Composable
internal fun QueryResult(component: QueryResultDTController) {

    val queryResponse = component.queryResponse
    RefineError(queryResponse)

    if (queryResponse.error == null) {
        DataTable(
            controller = component
        )
    }
}

@Composable
private fun RefineError(queryResponse: QueryResponse) {

    if (queryResponse.error != null) {
        Text(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            text = queryResponse.error,
            style = TextStyle(
                color = MaterialTheme.colorScheme.error
            )
        )
    }
}