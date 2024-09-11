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
import org.smartregister.fct.device_database.ui.components.QueryResultComponent
import org.smartregister.fct.device_database.ui.components.QueryTabComponent

@Composable
internal fun QueryResult(component: QueryResultComponent) {

    val result = component.result
    RefineError(result)

    if (result.isSuccess) {

    }
}

@Composable
private fun RefineError(result: Result<JSONObject>) {

    if (result.isFailure) {
        Text(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            text = result.exceptionOrNull()?.message ?: "Query Error",
            style = TextStyle(
                color = MaterialTheme.colorScheme.error
            )
        )
    }
}