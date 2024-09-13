package org.smartregister.fct.device_database.ui.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import org.smartregister.fct.datatable.presentation.ui.view.DataTable
import org.smartregister.fct.device_database.domain.model.QueryResponse
import org.smartregister.fct.device_database.ui.components.QueryResultDTController

@Composable
internal fun QueryResult(component: QueryResultDTController, componentContext: ComponentContext) {

    val queryResponse = component.queryResponse
    CheckError(queryResponse)

    if (queryResponse.error == null) {
        DataTable(
            controller = component,
            componentContext = componentContext
        )
    }
}

@Composable
private fun CheckError(queryResponse: QueryResponse) {

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